package sample.sparketl.etl.transformer.house;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.api.java.function.MapFunction;
import sample.sparketl.model.House;
import sample.sparketl.model.TypedEntity;
import sample.sparketl.utils.IdUtils;
import sample.sparketl.utils.JsonUtils;

import java.io.Serializable;
import java.util.Map;

@Log4j2
public class HouseTransformer implements Serializable {
    private final static long serialVersionUID = 400L;

    public MapFunction<TypedEntity, House> transform() {
        return new MapFunction<TypedEntity, House>() {
            private final static long serialVersionUID = 402L;
            @Override
            public House call(TypedEntity s) {
                try {
                    String data = s.getData();
                    if(StringUtils.isNotBlank(data)){
                        Map<String, Object> map = JsonUtils.getMap(data);
                        House house = new House();
                        String id = IdUtils.parseId(String.valueOf(JsonUtils.parse("url", map)));
                        house.setId(id);
                        house.setName(String.valueOf(JsonUtils.parse("name", map)));
                        house.setRegion(String.valueOf(JsonUtils.parse("region", map)));
                        house.setCoatofarms(String.valueOf(JsonUtils.parse("coatofarms", map)));
                        house.setWords(String.valueOf(JsonUtils.parse("words", map)));
                        house.setTitles(String.valueOf(JsonUtils.parse("titles", map)));
                        house.setSeats(String.valueOf(JsonUtils.parse("seats", map)));
                        house.setFounded(String.valueOf(JsonUtils.parse("founded", map)));
                        house.setFounder(String.valueOf(JsonUtils.parse("founder", map)));
                        house.setDiedout(String.valueOf(JsonUtils.parse("diedout", map)));
                        house.setAncestralweapons(String.valueOf(JsonUtils.parse("ancestralweapons", map)));
                        return house;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("Cannot parse log: " + s);
                }
                return new House();
            }
        };
    }
}