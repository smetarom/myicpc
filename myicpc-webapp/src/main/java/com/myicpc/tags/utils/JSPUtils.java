package com.myicpc.tags.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

/**
 * @author Roman Smetana
 */
public class JSPUtils {
    public static final String VALID_SCHEME_CHARS =	"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+.-";

    public static String resolveUrl(
            String url, PageContext pageContext)
            throws JspException {
        if (url == null) {
            return null;
        }
        // don't touch absolute URLs
        if (isAbsoluteUrl(url)) {
            return url;
        }

        // normalize relative URLs against a context root
        HttpServletRequest request =
                (HttpServletRequest) pageContext.getRequest();
        if (url.startsWith("/")) {
            return (request.getContextPath() + url);
        } else {
            return url;
        }
    }

    /**
     * Returns <tt>true</tt> if our current URL is absolute,
     * <tt>false</tt> otherwise.
     */
    public static boolean isAbsoluteUrl(String url) {
        // a null URL is not absolute, by our definition
        if (url == null)
            return false;

        // do a fast, simple check first
        int colonPos;
        if ((colonPos = url.indexOf(":")) == -1)
            return false;

        // if we DO have a colon, make sure that every character
        // leading up to it is a valid scheme character
        for (int i = 0; i < colonPos; i++)
            if (VALID_SCHEME_CHARS.indexOf(url.charAt(i)) == -1)
                return false;

        // if so, we've got an absolute url
        return true;
    }
}
