package com.myicpc.commons.utils;

import info.bliki.wiki.model.WikiModel;
import org.apache.commons.lang3.StringUtils;

/**
 * Util class for wirking with wiki syntax
 *
 * @author Roman Smetana
 */
public class WikiUtils {
    /**
     * Parses wiki text into HTML
     *
     * @param wikiText text containing wiki markup language
     * @return html representation of wiki text
     */
    public static String parseWikiSyntax(String wikiText) {
        if (StringUtils.isEmpty(wikiText)) {
            return wikiText;
        }
        return WikiModel.toHtml(wikiText);
    }
}
