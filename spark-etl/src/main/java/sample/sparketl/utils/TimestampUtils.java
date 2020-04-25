package sample.sparketl.utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimestampUtils {
    private final static String format = "yyyy-MM-dd'T'HH:mm:ss";
    public static Timestamp parseTimestamp(String ts){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDateTime localDateTime = LocalDateTime.from(formatter.parse(ts));
        return Timestamp.valueOf(localDateTime);
    }
}