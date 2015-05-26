package com.myicpc.controller;

import com.myicpc.commons.utils.CookieUtils;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.social.Notification;
import com.myicpc.service.contest.ContestService;
import com.myicpc.service.notification.NotificationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.site.SitePreference;
import org.springframework.mobile.device.site.SitePreferenceUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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

    @Autowired
    protected ContestService contestService;

    @Override
    protected Contest getContest(String contestCode, Model model) {
        Contest contest = super.getContest(contestCode, model);
        if (model != null && contest != null) {
            model.addAttribute("contestTime", contestService.getCurrentContestTime(contest));
            model.addAttribute("featuredNotifications", getFeaturedNotifications(contest));
        }
        return contest;
    }

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
     * @return list of featured notifications
     * @param contest
     */
    public List<Notification> getFeaturedNotifications(final Contest contest) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String ignoreFeaturedNotifications = CookieUtils.getCookie(request, "ignoreFeaturedNotifications");
        List<Long> ignoredFeatured = new ArrayList<>();
        ignoredFeatured.add(-1L);
        if (!StringUtils.isEmpty(ignoreFeaturedNotifications)) {
            try {
                String[] ss = ignoreFeaturedNotifications.split(",");
                for (String s : ss) {
                    ignoredFeatured.add(Long.parseLong(s));
                }
            } catch (Throwable ex) {
            }
        }
        return notificationService.getFeaturedNotifications(ignoredFeatured, contest);
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
