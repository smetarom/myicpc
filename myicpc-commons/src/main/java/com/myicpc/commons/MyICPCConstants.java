package com.myicpc.commons;

import java.text.SimpleDateFormat;

/**
 * Common constants in MyICPC
 *
 * @author Roman Smetana
 */
public class MyICPCConstants {
    /**
     * Default URL to CM university logo
     */
    public static final String UNIVERSITY_LOGO_URL = "http://icpc.baylor.edu/institution/logo/";
    /**
     * URL to custom CM university logo
     */
    public static final String CUSTOM_UNIVERSITY_LOGO_URL = "http://myicpc.icpcnews.com/cdn/%s/logo/%s";
    /**
     * The excel datetime pattern
     */
    public static final SimpleDateFormat EXCEL_DATE_TIME_FORMAT = new SimpleDateFormat("M/dd/yy HH:mm");
    /**
     * The excel date pattern
     */
    public static final SimpleDateFormat EXCEL_DATE_FORMAT = new SimpleDateFormat("M/dd/yy");
}
