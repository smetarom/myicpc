package com.myicpc.tags.utils;

/**
 * @author Roman Smetana
 */
public class HandlebarsUtils {
    public static String displayIfNotEmtpy(String variable, String body) {
        return String.format("{{#if %s}}%s{{/if}}", variable, body);
    }
}
