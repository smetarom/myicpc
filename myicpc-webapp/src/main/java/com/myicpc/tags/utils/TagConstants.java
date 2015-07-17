package com.myicpc.tags.utils;

/**
 * @author Roman Smetana
 */
public class TagConstants {
    public static final String IMAGE_FORMAT = "<img src=\"%s\" alt=\"\" class=\"img-responsive center-block\" style=\"max-height: 300px;\" />";
    public static final String VIDEO_FORMAT = "<video src=\"%s\" poster=\"%s\" controls muted loop width=\"100%%\" style=\"max-height: 300px;\">\n" +
            "  Your browser does not support the video player.\n" +
            "</video>";
    public static final String VIDEO_FORMAT_AUTOPLAY = "<video src=\"%s\" poster=\"%s\" controls muted loop autoplay width=\"100%%\" style=\"max-height: 300px;\">\n" +
            "  Your browser does not support the video player.\n" +
            "</video>";
}
