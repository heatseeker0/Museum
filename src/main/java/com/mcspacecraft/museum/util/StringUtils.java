package com.mcspacecraft.museum.util;

import java.util.Collection;

public class StringUtils {
    public static String join(Collection<String> collection, String separator) {
        StringBuilder sb = new StringBuilder();

        boolean first = true;
        for (String str : collection) {
            if (first) {
                first = false;
            } else {
                sb.append(separator);
            }

            sb.append(str);
        }

        return sb.toString();
    }
}
