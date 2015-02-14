package com.myicpc.controller;

import com.myicpc.commons.utils.CookieUtils;
import com.myicpc.model.social.Notification;
import com.myicpc.service.notification.NotificationService;
import com.myicpc.service.notification.NotificationServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.site.SitePreference;
import org.springframework.mobile.device.site.SitePreferenceUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * General controller, which is responsible for handling exceptions and populate
 * model with object, we want to have in all public views
 *
 * @author Roman Smetana
 */
public abstract class GeneralController extends GeneralAbstractController {
    @Autowired
    protected NotificationService notificationService;

    /**
     * Populates model with site preference
     *
     * @return user site preferences
     */
    @ModelAttribute("sitePreference")
    public SitePreference getSitePreferences(HttpServletRequest request) {
        return SitePreferenceUtils.getCurrentSitePreference(request);
    }

    /**
     * Populates model with featured notifications
     *
     * @param request
     * @param response
     * @param ignoreFeaturedNotifications
     * @return list of featured notifications
     */
    @ModelAttribute("featuredNotifications")
    public List<Notification> getFeaturedNotifications(HttpServletRequest request, HttpServletResponse response,
                                                       @CookieValue(value = "ignoreFeaturedNotifications", required = false) String ignoreFeaturedNotifications) {
        List<Long> ignoredFeatured = new ArrayList<Long>();
        ignoredFeatured.add(-1L);
        if (!StringUtils.isEmpty(ignoreFeaturedNotifications)) {
            try {
                String[] ss = ignoreFeaturedNotifications.split(",");
                for (String s : ss) {
                    ignoredFeatured.add(Long.parseLong(s));
                }
            } catch (Throwable ex) {
                CookieUtils.removeCookie(request, response, "ignoreFeaturedNotifications");
            }
        }
        return notificationService.getFeaturedNotifications(ignoredFeatured);
    }

    protected String resolveView(@NotNull String desktopView, String mobileView, SitePreference sitePreference) {
        return resolveView(desktopView, mobileView, null, sitePreference);
    }

    protected String resolveView(@NotNull String desktopView, String mobileView, String tabletView, SitePreference sitePreference) {
        if (sitePreference == null) {
            return desktopView;
        }
        if (sitePreference.isMobile()) {
            return !StringUtils.isEmpty(mobileView) ? mobileView : desktopView;
        }
        if (sitePreference.isTablet()) {
            return !StringUtils.isEmpty(tabletView) ? tabletView : desktopView;
        }
        return desktopView;
    }
}
