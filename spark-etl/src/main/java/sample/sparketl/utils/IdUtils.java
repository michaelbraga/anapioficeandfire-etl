package sample.sparketl.utils;

import org.apache.commons.lang.StringUtils;

public class IdUtils {
    public static String parseId(String url) {
        if(StringUtils.isBlank(url))
            return null;
        String[] p = url.split("/");
        return p[p.length-1];
    }
}