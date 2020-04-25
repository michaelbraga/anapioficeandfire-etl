package sample.sparketl.etl.transformer.character;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.api.java.function.MapFunction;
import sample.sparketl.model.Character;
import sample.sparketl.model.TypedEntity;
import sample.sparketl.utils.IdUtils;
import sample.sparketl.utils.JsonUtils;

import java.io.Serializable;
import java.util.Map;

@Log4j2
public class CharacterTransformer implements Serializable {
    private final static long serialVersionUID = 300L;

    public MapFunction<TypedEntity, Character> transform() {
        return new MapFunction<TypedEntity, Character>() {
            private final static long serialVersionUID = 302L;
            @Override
            public Character call(TypedEntity s) {
                try {
                    String data = s.getData();
                    if(StringUtils.isNotBlank(data)){
                        Map<String, Object> map = JsonUtils.getMap(data);
                        Character character = new Character();
                        String id = IdUtils.parseId(String.valueOf(JsonUtils.parse("url", map)));
                        character.setId(id);
                        character.setName(String.valueOf(JsonUtils.parse("name", map)));
                        character.setGender(String.valueOf(JsonUtils.parse("gender", map)));
                        character.setCulture(String.valueOf(JsonUtils.parse("culture", map)));
                        character.setBorn(String.valueOf(JsonUtils.parse("born", map)));
                        character.setTitles(String.valueOf(JsonUtils.parse("titles", map)));
                        character.setAliases(String.valueOf(JsonUtils.parse("aliases", map)));
                        character.setMother(String.valueOf(JsonUtils.parse("mother", map)));
                        character.setFather(String.valueOf(JsonUtils.parse("father", map)));
                        character.setSpouse(String.valueOf(JsonUtils.parse("spouse", map)));
                        character.setTvseries(String.valueOf(JsonUtils.parse("tvseries", map)));
                        character.setPlayedby(String.valueOf(JsonUtils.parse("playedby", map)));
                        return character;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("Cannot parse log: " + s);
                }
                return new Character();
            }
        };
    }
}