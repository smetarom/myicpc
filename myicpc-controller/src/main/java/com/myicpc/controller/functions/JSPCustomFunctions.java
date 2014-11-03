package com.myicpc.controller.functions;

import com.myicpc.commons.utils.FormatUtils;
import com.myicpc.commons.utils.WikiUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.jsp.jstl.core.LoopTagStatus;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author Roman Smetana
 */
public class JSPCustomFunctions {
    /**
     * Encodes URL in UTF-8
     *
     * @param value URL to encode
     * @return encoded URL
     * @throws UnsupportedEncodingException UTF-8 is not supported
     */
    public static String urlEncode(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, "UTF-8");
    }

    /**
     * Make full address, when http:// or https:// is omitted
     *
     * @param url original URL
     * @return full URL
     */
    public static String correctURL(String url) {
        if (StringUtils.isEmpty(url)) {
            return url;
        }
        if (url.startsWith("http://") || url.startsWith("https://")) {
            return url;
        }
        return "http://" + url;
    }

    /**
     * @param value text with HTML tags
     * @return text wintout HTML tags
     * @see FormatUtils#removeHTMLTags(String)
     */
    public static String escapeHTML(String value) {
        return FormatUtils.removeHTMLTags(value);
    }

    /**
     * @param url URL to prepend
     * @return full URL
     * @see FormatUtils#prependHTTPToURL(String)
     */
    public static String prependHTTP(String url) {
        return FormatUtils.prependHTTPToURL(url);
    }

    /**
     * Is the index odd
     *
     * @param status loop JSP status
     * @return if index is odd
     */
    public static boolean isOddLine(LoopTagStatus status) {
        if (status == null) {
            return false;
        }
        return status.getIndex() % 2 != 0;
    }

    /**
     * Is the index odd
     *
     * @param status loop JSP status
     * @return if index is odd
     */
    public static boolean isNLine(LoopTagStatus status, int n, int from) {
        if (status == null) {
            return false;
        }
        return status.getIndex() % from == n;
    }

    /**
     * Parses wiki text into HTML
     *
     * @param wikiText text containing wiki markup language
     * @return html representation of wiki text
     */
    public static String parseWikiSyntax(String wikiText) {
        return WikiUtils.parseWikiSyntax(wikiText);
    }

    /**
     * Escapes JavaScript characters
     *
     * @param javascriptText text containing JavaScript characters
     * @return text with escaped JavaScript characters
     */
    public static String escapeJavascript(String javascriptText) {
        return StringEscapeUtils.escapeEcmaScript(javascriptText);
    }

}
