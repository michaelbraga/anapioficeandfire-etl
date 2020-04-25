package sample.sparketl.spark;

import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sample.sparketl.config.SparkConfig;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Log4j
@Component
public class EtlSparkSession {
    @Autowired
    private SparkConfig sparkConfig;
    @Getter
    private SparkSession sparkSession;

    @PostConstruct
    private void createSparkSession(){
        SparkConf conf = new SparkConf();
        conf.setMaster(sparkConfig.getMaster());
        conf.setAppName(sparkConfig.getAppName());
        for(String c : sparkConfig.getConfigurations()){
            if(c.contains("=")){
                String[] confParts = c.split("=");
                conf.set(confParts[0], confParts[1]);
            }
        }
        sparkSession = SparkSession.builder().config(conf).getOrCreate();
        sparkSession.sparkContext().setLogLevel("ERROR");
        log.info("Created spark session");
        log.info("Spark Configuration: " + sparkConfig.toString());
    }

    @PreDestroy
    private void destroySparkSession(){
        if(sparkSession != null){
            try {
                sparkSession.close();
            } catch (Exception e){
                log.warn("Failed shutting down spark session: " + e.getMessage());
            }
        }
    }
}