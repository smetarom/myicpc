package com.myicpc.commons.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Set;

/**
 * Util class for easy work with HTTP cookies
 *
 * @author Roman Smetana
 */
public class CookieUtils {
    /**
     * Returns value of the cookie
     *
     * @param request    HTTP request
     * @param cookieName cookie name
     * @return cookie value
     */
    public static String getCookie(final HttpServletRequest request, final String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equalsIgnoreCase(cookieName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * Set value of cookie
     *
     * @param request    HTTP request
     * @param response   HTTP response
     * @param cookieName cookie name
     * @param value      cookie value
     */
    public static void setCookie(final HttpServletRequest request, final HttpServletResponse response, final String cookieName, final String value) {
        Cookie c = new Cookie(cookieName, value);
        c.setPath(StringUtils.isEmpty(request.getContextPath()) ? "/" : request.getContextPath());
        c.setMaxAge(604800); // 1 week
        response.addCookie(c);
    }

    /**
     * Remove cookie
     *
     * @param request    HTTP request
     * @param response   HTTP response
     * @param cookieName cookie name
     */
    public static void removeCookie(final HttpServletRequest request, final HttpServletResponse response, final String cookieName) {
        Cookie c = new Cookie(cookieName, null);
        c.setPath(StringUtils.isEmpty(request.getContextPath()) ? "/" : request.getContextPath());
        c.setMaxAge(-1);
        response.addCookie(c);
    }

    /**
     * Append ID to the cookie variable
     *
     * @param cookieValue cookie value
     * @param cookieName  cookie name
     * @param addedId     ID to append
     * @param request http request
     * @param response http response
     */
    public static void appendIdToCookie(final String cookieValue, final String cookieName, final String addedId, final HttpServletRequest request,
                                        final HttpServletResponse response) {
        String cookie;
        if (StringUtils.isEmpty(cookieValue)) {
            cookie = addedId;
        } else {
            String[] ss = cookieValue.split(",");
            boolean contains = false;
            for (String s : ss) {
                if (s.equals(addedId)) {
                    contains = true;
                }
            }
            cookie = cookieValue;
            if (!contains) {
                cookie += "," + addedId;
            }
        }
        CookieUtils.setCookie(request, response, cookieName, cookie);
    }

    /**
     * Parses the number values separated by comma
     *
     * Ignores non-numeric values and continue parsing
     *
     * @param cookieValue cookie value
     * @return set of numbers
     */
    public static Set<Long> getCookieAsLongs(final String cookieValue) {
        Set<Long> list = new HashSet<>();
        if (StringUtils.isEmpty(cookieValue)) {
            return list;
        }
        String[] ss = cookieValue.split(",");
        for (String s : ss) {
            try {
                list.add(Long.parseLong(s));
            } catch (NumberFormatException ex) {
                // ignore non-number expression
            }
        }
        return list;
    }

    /**
     * Append ID to the cookie variable
     *
     * @param cookieValue cookie value
     * @param cookieName  cookie name
     * @param addedId     ID to append
     * @param request http request
     * @param response http response
     */
    public static void removeIdToCookie(final String cookieValue, final String cookieName, final String addedId, final HttpServletRequest request,
                                        final HttpServletResponse response) {
        if (!StringUtils.isEmpty(cookieValue)) {
            StringBuilder sb = new StringBuilder();
            String[] ss = cookieValue.split(",");
            for (String s : ss) {
                if (!s.equals(addedId)) {
                    sb.append(s).append(",");
                }
            }
            String c = sb.toString();
            if (StringUtils.isEmpty(c)) {
                CookieUtils.removeCookie(request, response, cookieName);
            } else {
                c = c.substring(0, c.length() - 1);
                CookieUtils.setCookie(request, response, cookieName, c);
            }
        }
    }
}
