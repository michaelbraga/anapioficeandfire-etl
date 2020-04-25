package sample.sparketl.etl.transformer.book;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.api.java.function.MapFunction;
import sample.sparketl.model.Book;
import sample.sparketl.model.TypedEntity;
import sample.sparketl.utils.IdUtils;
import sample.sparketl.utils.JsonUtils;
import sample.sparketl.utils.TimestampUtils;

import java.io.Serializable;
import java.util.Map;

@Log4j2
public class BookTransformer implements Serializable {
    private final static long serialVersionUID = 200L;

    public MapFunction<TypedEntity, Book> transform() {
        return new MapFunction<TypedEntity, Book>() {
            private final static long serialVersionUID = 202L;
            @Override
            public Book call(TypedEntity s) {
                try {
                    String data = s.getData();
                    if(StringUtils.isNotBlank(data)){
                        Map<String, Object> map = JsonUtils.getMap(data);
                        Book book = new Book();
                        String id = IdUtils.parseId(String.valueOf(JsonUtils.parse("url", map)));
                        book.setId(id);
                        book.setName(String.valueOf(JsonUtils.parse("name", map)));
                        book.setIsbn(String.valueOf(JsonUtils.parse("isbn", map)));
                        book.setAuthors(String.valueOf(JsonUtils.parse("authors", map)));
                        book.setNumberofpages(Integer.parseInt(String.valueOf(JsonUtils.parse("numberofpages", map))));
                        book.setPublisher(String.valueOf(JsonUtils.parse("publisher", map)));
                        book.setCountry(String.valueOf(JsonUtils.parse("country", map)));
                        book.setMediatype(String.valueOf(JsonUtils.parse("mediatype", map)));
                        book.setReleased(TimestampUtils.parseTimestamp(String.valueOf(JsonUtils.parse("released", map))));
                        return book;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("Cannot parse log: " + s);
                }
                return new Book();
            }
        };
    }
}