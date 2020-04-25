package sample.apiextractor.services;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sample.apiextractor.api.ApiUtils;
import sample.apiextractor.config.CacheConfig;
import sample.apiextractor.kafka.KafkaProducerService;
import sample.apiextractor.utils.UrlParser;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
public class Crawler {

    @Autowired
    private KafkaProducerService kafkaProducerService;
    @Autowired
    private CacheConfig cacheConfig;
    private Map<String, Boolean> graph;
    private Deque<String> toBeVisited;
    private Map<String, Boolean> toBeVisitedMap;

    @PostConstruct
    private void initialize(){
        graph = new HashMap<>();
        toBeVisited = new ArrayDeque<>();
        toBeVisitedMap = new HashMap<>();
    }

    public void crawl(String rootUrl) throws IOException {
        // perform DFS
        addToBeVisited(rootUrl);
        while (toBeVisited.size() > 0){
            String nodeUrl = visitANode();
            if(!isVisited(nodeUrl)) {
                updateVisited(nodeUrl);
                String raw = analyzeData(nodeUrl);
                List<String> neighbors = getNeighbors(raw);
                addNotVisitedNeighbors(neighbors);
            }
        }
    }

    private String visitANode() {
        String node = toBeVisited.poll();
        toBeVisitedMap.remove(node);
        return node;
    }

    private void addToBeVisited(String url) {
        if(!toBeVisitedMap.containsKey(url)){
            toBeVisited.add(url);
            toBeVisitedMap.put(url, true);
        }
    }

    private void addNotVisitedNeighbors(List<String> neighbors) {
        neighbors.forEach(neighbor -> {
            try {
                if(!isVisited(neighbor)){
                    addToBeVisited(neighbor);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        });
    }

    private boolean isVisited(String url) throws MalformedURLException {
        String path = new URL(url).getPath();
        return graph.containsKey(path);
    }

    private void updateVisited(String url) throws MalformedURLException {
        String path = new URL(url).getPath();
        if(!graph.containsKey(path)) {
            graph.put(path, true);
            log.info("Visited: " + path + " : " + graph.containsKey(path));
        }
    }

    private String analyzeData(String nodeUrl) throws IOException {
        String rawData = retrieveData(nodeUrl);
        String path = new URL(nodeUrl).getPath();
        if(checkIfEntity(path)){
            // send to kafka
            kafkaProducerService.sendMessageToKafka(rawData);
        }
        return rawData;
    }

    private boolean checkIfEntity(String path) {
        // path that ends with a number is an entity
        return Character.isDigit(path.charAt(path.length()-1));
    }

    private String retrieveData(String nodeUrl) throws IOException {
        String localPath = getCacheLocation(nodeUrl);
        localPath = localPath + "/data.json";
        if(Paths.get(localPath).toFile().exists()){
            log.info("Retrieving data from cache for: " + nodeUrl);
            return new String (Files.readAllBytes(Paths.get(localPath)));
        }
        String rawData = ApiUtils.get(nodeUrl);
        saveForCaching(nodeUrl, rawData);
        return rawData;
    }

    private String getCacheLocation(String nodeUrl) throws MalformedURLException {
        String basePath = cacheConfig.getLocation();
        String path = new URL(nodeUrl).getPath();
        return basePath + path;
    }

    private void saveForCaching(String nodeUrl, String rawData) throws IOException {
        String dir = getCacheLocation(nodeUrl);
        File dirFile = new File(dir);
        if(!dirFile.exists())
            dirFile.mkdirs();
        Files.write(Paths.get(dir + "/data.json"), rawData.getBytes());
    }

    private List<String> getNeighbors(String data) {
        return UrlParser.extractUrls(data);
    }
}
