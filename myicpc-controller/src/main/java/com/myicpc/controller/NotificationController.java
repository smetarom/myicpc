package com.myicpc.controller;

import com.myicpc.commons.utils.CookieUtils;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.social.Notification;
import com.myicpc.repository.social.NotificationRepository;
import com.myicpc.service.notification.NotificationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Roman Smetana
 */
@Controller
public class NotificationController extends GeneralController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @RequestMapping(value = "/{contestCode}/notifications", method = RequestMethod.GET)
    public String featuredNotifications(Model model, @PathVariable String contestCode,
                                        @CookieValue(required = false) String ignoreFeaturedNotifications) {
        Contest contest = getContest(contestCode, model);
        List<Long> ignoredFeatured = new ArrayList<>();
        ignoredFeatured.add(-1L);
        if (!StringUtils.isEmpty(ignoreFeaturedNotifications)) {
            ignoredFeatured.addAll(CookieUtils.getCookieAsLongs(ignoreFeaturedNotifications));
        }
        Iterable<Notification> featuredNotifications = notificationService.getFeaturedNotifications(ignoredFeatured, contest);
        model.addAttribute("notifications", featuredNotifications);
        model.addAttribute("availableNotificationTypes", NotificationService.FEATURED_NOTIFICATION_TYPES);
        return "notification/notifications";
    }

    @RequestMapping(value = "/{contestCode}/notification/featured-panel", method = RequestMethod.GET)
    public String featuredNotificationsPanel(Model model, @PathVariable String contestCode,
                                             @CookieValue(required = false) String ignoreFeaturedNotifications) {
        Contest contest = getContest(contestCode, model);
        List<Long> ignoredFeatured = new ArrayList<>();
        ignoredFeatured.add(-1L);
        if (!StringUtils.isEmpty(ignoreFeaturedNotifications)) {
            ignoredFeatured.addAll(CookieUtils.getCookieAsLongs(ignoreFeaturedNotifications));
        }
        Pageable pageable = new PageRequest(0, 5);
        Iterable<Notification> featuredNotifications = notificationService.getFeaturedNotifications(ignoredFeatured, contest, pageable);

        model.addAttribute("featuredNotifications", featuredNotifications);
        return "notification/notificationPanel";
    }

    @RequestMapping(value = "/{contestCode}/notification/hashtag-panel", method = RequestMethod.GET)
    public String hashtagPanel(Model model, @PathVariable String contestCode,
                               @RequestParam String hashtag1,
                               @RequestParam(required = false) String hashtag2) {
        Contest contest = getContest(contestCode, model);

        if (hashtag2 == null) {
            hashtag2 = StringUtils.EMPTY;
        }
        List<Notification> notifications = notificationRepository.findByHashTagsAndContest(hashtag1, hashtag2, contest);

        model.addAttribute("notifications", notifications);
        model.addAttribute("hashtag1", hashtag1.substring(1, hashtag1.length()-1));
        model.addAttribute("hashtag2", hashtag2.substring(1, hashtag2.length()-1));
        return "notification/hashtagPanel";
    }

    @RequestMapping(value = "/{contestCode}/notification/{notificationId}/share", method = RequestMethod.GET)
    public String hashtagPanel(Model model, @PathVariable String contestCode,
                               @PathVariable Long notificationId) {
        Contest contest = getContest(contestCode, model);
        model.addAttribute("notification", notificationRepository.findOne(notificationId));
        return "notification/shareDialog";
    }
}
