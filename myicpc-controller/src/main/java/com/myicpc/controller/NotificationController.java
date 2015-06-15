package com.myicpc.controller;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.social.Notification;
import com.myicpc.repository.social.NotificationRepository;
import com.myicpc.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Roman Smetana
 */
@Controller
public class NotificationController extends GeneralController {
    @Autowired
    private NotificationRepository notificationRepository;

    @RequestMapping(value = "/{contestCode}/notifications", method = RequestMethod.GET)
    public String featuredNotifications(Model model, @PathVariable String contestCode) {
        Contest contest = getContest(contestCode, model);
        Iterable<Notification> featuredNotifications = notificationRepository.findAll();
        model.addAttribute("notifications", featuredNotifications);
        model.addAttribute("availableNotificationTypes", NotificationService.FEATURED_NOTIFICATION_TYPES);
        return "notification/notifications";
    }

    @RequestMapping(value = "/{contestCode}/notification/featured-panel", method = RequestMethod.GET)
    public String featuredNotificationsPanel(Model model, @PathVariable String contestCode) {
        Contest contest = getContest(contestCode, model);
        Iterable<Notification> featuredNotifications = notificationRepository.findAll();
//        Iterable<Notification> featuredNotifications = new ArrayList<>();

        model.addAttribute("featuredNotifications", featuredNotifications);
        return "notification/notificationPanel";
    }
}
