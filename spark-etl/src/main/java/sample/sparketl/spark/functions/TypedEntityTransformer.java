package sample.sparketl.spark.functions;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.api.java.function.MapFunction;
import sample.sparketl.model.TypedEntity;

import java.io.Serializable;
import java.util.Map;

@Data
@Log4j2
public class TypedEntityTransformer implements Serializable {
    private final static long serialVersionUID = 101L;

    public static MapFunction<String, TypedEntity> transform() {
        return new MapFunction<String, TypedEntity>() {
            private final static long serialVersionUID = 102L;
            @Override
            public TypedEntity call(String s) throws Exception {
                try {
                    if(StringUtils.isNotBlank(s)){
                        ObjectMapper mapper = new ObjectMapper();
                        Map<String, Object> map = mapper.readValue(s, Map.class);
                        String url = (String) map.get("url");
                        String[] parts = url.split("/");
                        String type = parts[parts.length-2];
                        return new TypedEntity(type, s);
                    }
                } catch (Exception e) {
                    log.error("Cannot parse log: " + s);
                }
                return new TypedEntity();
            }
        };
    }
}