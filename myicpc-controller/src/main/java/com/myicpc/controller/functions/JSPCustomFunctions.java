package com.myicpc.controller.functions;

import com.myicpc.commons.MyICPCConstants;
import com.myicpc.commons.utils.MessageUtils;
import com.myicpc.commons.utils.FormatUtils;
import com.myicpc.model.contest.Contest;
import info.bliki.wiki.model.WikiModel;
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
     * @see com.myicpc.commons.utils.FormatUtils#removeHTMLTags(String)
     */
    public static String escapeHTML(String value) {
        return FormatUtils.removeHTMLTags(value);
    }

    /**
     * @param url URL to prepend
     * @return full URL
     * @see com.myicpc.commons.utils.FormatUtils#prependHTTPToURL(String)
     */
    public static String prependHTTP(String url) {
        return FormatUtils.prependHTTPToURL(url);
    }

    public static String formatMinutes(int minutes) {
        if (minutes < 60) {
            return MessageUtils.getMessage("mm.shortcut", minutes);
        } else {
            int hours = minutes / 60;
            minutes %= 60;
            return MessageUtils.getMessage("HHmm.shortcut", hours, minutes);
        }
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
        if (StringUtils.isEmpty(wikiText)) {
            return wikiText;
        }
        return WikiModel.toHtml(wikiText);
    }

    public static boolean arrayContains(Object[] objects, Object object) {
        if (objects == null || object == null) {
            return false;
        }
        for (Object o : objects) {
            if (object.equals(o)) {
                return true;
            }
        }
        return false;
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

    public static String universityLogoUrl(Long universityExternalId) {
        return MyICPCConstants.UNIVERSITY_LOGO_URL + universityExternalId;
    }

    public static boolean isMapModuleEnabled(Contest contest) {
        if (contest != null && contest.getModuleConfiguration() != null) {
            return contest.getModuleConfiguration().isMapModule();
        }
        return false;
    }

    public static boolean isCodeInsightModuleEnabled(Contest contest) {
        if (contest != null && contest.getModuleConfiguration() != null) {
            return contest.getModuleConfiguration().isCodeInsightModule();
        }
        return false;
    }

    public static boolean isScheduleModuleEnabled(Contest contest) {
        if (contest != null && contest.getModuleConfiguration() != null) {
            return contest.getModuleConfiguration().isScheduleModule();
        }
        return false;
    }

    public static boolean isQuestModuleEnabled(Contest contest) {
        if (contest != null && contest.getModuleConfiguration() != null) {
            return contest.getModuleConfiguration().isQuestModule();
        }
        return false;
    }

    public static boolean isTechTrekModuleEnabled(Contest contest) {
        if (contest != null && contest.getModuleConfiguration() != null) {
            return contest.getModuleConfiguration().isTechtrekModule();
        }
        return false;
    }

    public static boolean isRssModuleEnabled(Contest contest) {
        if (contest != null && contest.getModuleConfiguration() != null) {
            return contest.getModuleConfiguration().isRssModule();
        }
        return false;
    }

    public static boolean isGalleryModuleEnabled(Contest contest) {
        if (contest != null && contest.getModuleConfiguration() != null) {
            return contest.getModuleConfiguration().isGalleryModule();
        }
        return false;
    }

    public static boolean isPollModuleEnabled(Contest contest) {
        if (contest != null && contest.getModuleConfiguration() != null) {
            return contest.getModuleConfiguration().isPollModule();
        }
        return false;
    }
}
