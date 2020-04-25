package sample.sparketl.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class JsonUtils {
    public static Map<String, Object> getMap(String data) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(data, Map.class);
        Map<String, Object> copy = new HashMap<>();
        map.keySet().forEach(key -> {
            copy.put(key.toLowerCase(), map.get(key));
        });
        map.clear();
        return copy;
    }

    public static Object parse(String key, Map<String, Object> map) {
        try {
            Object str =  map.get(key);
            if("[]".equals(String.valueOf(str)))
                return "";
            return str;
        } catch (Exception e) {
            return null;
        }
    }
}