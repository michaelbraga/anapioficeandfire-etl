package sample.apiextractor.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlParser {
    private static final String URL_REGEX = "\\(?\\b(http(s)?://|www[.])[-A-Za-z0-9+&amp;@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&amp;@#/%=~_()|]";
    private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);

    public static List<String> extractUrls(String str) {
        Matcher matcher = URL_PATTERN.matcher(str);
        List<String> urls = new ArrayList<>();
        while (matcher.find()) {
            urls.add(matcher.group());
        }
        return urls;
    }
}
