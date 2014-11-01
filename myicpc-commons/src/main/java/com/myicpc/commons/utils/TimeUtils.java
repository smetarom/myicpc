package com.myicpc.commons.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Roman Smetana
 */
public class TimeUtils {
    /**
     * Convert local date to UTC date
     *
     * @param localDate       local date
     * @param contestTimeDiff time difference between UTC and local timezone
     * @return UTC date
     */
    public static Date convertLocalDateToUTC(final Date localDate, final int contestTimeDiff) {
        if (localDate == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(localDate);
        cal.add(Calendar.MINUTE, -1 * contestTimeDiff);
        return cal.getTime();
    }

    /**
     * Convert UTC date to local date
     *
     * @param utcDate         UTC date
     * @param contestTimeDiff time difference between UTC and local timezone
     * @return local date
     */
    public static Date convertUTCDateToLocal(final Date utcDate, final int contestTimeDiff) {
        if (utcDate == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(utcDate);
        cal.add(Calendar.MINUTE, contestTimeDiff);
        return cal.getTime();
    }

    /**
     * Date format yyyy-MM-dd HH:mm:ss
     *
     * @return date formatter
     */
    public static SimpleDateFormat getDateTimeFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * Date format yyyy-MM-dd
     *
     * @return date formatter
     */
    public static SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd");
    }

    /**
     * Date format HH:mm
     *
     * @return date formatter
     */
    public static SimpleDateFormat getTimeFormat() {
        return new SimpleDateFormat("HH:mm");
    }

    /**
     * Date format M/dd/yy HH:mm
     *
     * @return date formatter
     */
    public static SimpleDateFormat getExcelDateTimeFormat() {
        return new SimpleDateFormat("M/dd/yy HH:mm");
    }

    /**
     * Date format M/dd/yy
     *
     * @return date formatter
     */
    public static SimpleDateFormat getExcelDateFormat() {
        return new SimpleDateFormat("M/dd/yy");
    }
}
