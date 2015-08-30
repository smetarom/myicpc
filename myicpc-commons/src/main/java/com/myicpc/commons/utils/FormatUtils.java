package com.myicpc.commons.utils;

import com.google.common.base.Charsets;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Utils for formatting text, numbers and dates
 *
 * @author Roman Smetana
 */
public class FormatUtils {
    public static final Charset DEFAULT_ENCODING = Charsets.UTF_8;

    /**
     * Mapping between word and abbreviation for team names
     */
    private static final Map<String, String> teamMapping;
    /**
     * Mapping between word and abbreviation for region names
     */
    private static final Map<String, String> regionMapping;

    /**
     * Common patterns
     */
    static {
        teamMapping = new HashMap<>();
        teamMapping.put("University", "Univ.");
        teamMapping.put("Universidad", "Univ.");
        teamMapping.put("Universidade", "Univ.");
        teamMapping.put("Mechanics", "Mech.");
        teamMapping.put("Optics", "Opt.");
        teamMapping.put("National", "Natl.");
        teamMapping.put("Technology", "Tech.");
        teamMapping.put("Technical", "Tech.");
        teamMapping.put("Institute", "Inst.");
        teamMapping.put("Informatics", "Inf.");
        teamMapping.put("Mathematics", "Math.");
        teamMapping.put("Engineering", "Eng.");
        teamMapping.put("Graduate", "Grad.");

        regionMapping = new HashMap<>();
        regionMapping.put("Middle", "M.");
        regionMapping.put("and", "&");
        regionMapping.put("the", "");
        regionMapping.put("Latin", "Lat.");
        regionMapping.put("North", "N.");
        regionMapping.put("South", "S.");
    }

    public static String removeHashFromHashtag(String hashtag) {
        if (StringUtils.isEmpty(hashtag)) {
            return hashtag;
        }
        if (hashtag.charAt(0) == '#') {
            return hashtag.substring(1);
        }
        return hashtag;
    }

    /**
     * Round double
     *
     * @param value   value
     * @param decimal number of result decimal
     * @return rounded number
     */
    public static double roundDouble(final double value, final int decimal) {
        BigDecimal d = new BigDecimal(value);
        return d.setScale(decimal, RoundingMode.HALF_DOWN).doubleValue();
    }

    /**
     * Format number of seconds to hours and minutes
     *
     * @param time time in seconds
     * @return Xh XXmin
     */
    public static String formatTimeToHoursMinutes(final Double time) {
        if (time == null) {
            return "";
        }
        int hours = (int) Math.floor(time / 3600);
        int min = (int) Math.floor(time / 60) % 60;
        StringBuilder sb = new StringBuilder();
        if (hours > 0) {
            sb.append(hours).append("h ");
        }
        return sb.append(min).append("min").toString();
    }

    /**
     * Return number of minutes
     *
     * @param time time in seconds
     * @return time in minutes
     */
    public static String formatTimeToMinutes(final Double time) {
        return Integer.toString((int) Math.floor(time / 60));
    }

    /**
     * Format number of seconds to hours and minutes
     *
     * @param time time in seconds
     * @return HH:mm
     */
    public static String formatContestTime(final Double time) {
        if (time == null) {
            return "00:00";
        }
        int hours = (int) Math.floor(time / 3600);
        int min = (int) Math.floor(time / 60) % 60;
        return String.format("%02d:%02d", hours, min);
    }

    /**
     * Return number of minutes
     *
     * @param time time in seconds
     * @return time in minutes
     */
    public static Integer calcTimeInMinutes(final Double time) {
        return (int) Math.floor(time / 60);
    }

    /**
     * Formats the rank
     *
     * @param rank rank
     * @return formatted rank
     */
    public static String prettyRank(final int rank) {
        int j = rank % 10;
        if (j == 1 && rank != 11) {
            return rank + "st";
        }
        if (j == 2 && rank != 12) {
            return rank + "nd";
        }
        if (j == 3 && rank != 13) {
            return rank + "rd";
        }
        return rank + "th";
    }

    public static String clearHashtag(String hashtag) {
        if (StringUtils.isEmpty(hashtag)) return null;
        return hashtag.charAt(0) == '#' ? hashtag.substring(1) : hashtag;
    }

    /**
     * Get short name of the team
     *
     * @param teamName full team name
     * @return short team name
     */
    public static String getTeamShortName(final String teamName) {
        return StringUtils.abbreviate(teamName, 40);
    }

    /**
     * Get short region name
     *
     * @param regionName full region name
     * @return short region name
     */
    public static String getRegionShortName(final String regionName) {
        return getShortName(regionName, regionMapping, 20);
    }

    /**
     * Replaces known words with abbreviations
     *
     * @param original  original name
     * @param mapping   known words and abbreviations
     * @param maxLength max length of the product
     * @return shorten name
     */
    private static String getShortName(final String original, final Map<String, String> mapping, final int maxLength) {
        if (StringUtils.isEmpty(original) || original.length() <= maxLength) {
            return original;
        }
        int length = original.length();
        String[] ss = original.split("\\s+");
        for (int j = 0; j < ss.length; j++) {
            if (mapping.containsKey(ss[j])) {
                length -= ss[j].length() - mapping.get(ss[j]).length();
                ss[j] = mapping.get(ss[j]);
            }
            if (length <= maxLength) {
                break;
            }
        }

        String shortName = StringUtils.join(ss, " ");
        return StringUtils.abbreviate(shortName, maxLength);
    }

    /**
     * Creates abbreviation by taking first letters of the words
     *
     * @param name name to be abbreviated
     * @return abbreviation
     */
    public static String getAbbreviation(final String name) {
        StringBuilder abbreviation = new StringBuilder();
        String[] ss = name.split("\\s+");
        for (String s : ss) {
            abbreviation.append(s.charAt(0));
        }
        return abbreviation.toString();
    }

    /**
     * Creates hashtag from the name
     *
     * @param name name to be hashtagged
     * @return hashtag
     */
    public static String getHashtag(final String name) {
        return name.replaceAll("\\W", "");
    }

    /**
     * Remove HTML tags from text
     *
     * @param witHTML HTML text
     * @return text without HTML tags
     */
    public static String removeHTMLTags(String witHTML) {
        return witHTML.replaceAll("\\<[^>]*>", "");
    }

    /**
     * Prepend http to the URL, if it does not start with http
     *
     * @param url URL to prepend
     * @return full URL
     */
    public static String prependHTTPToURL(String url) {
        if (StringUtils.isEmpty(url) || url.startsWith("http")) {
            return url;
        }
        return "http:" + url;
    }
}

