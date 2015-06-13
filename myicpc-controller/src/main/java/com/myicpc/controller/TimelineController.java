package com.myicpc.controller;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.schedule.Event;
import com.myicpc.model.social.Notification;
import com.myicpc.service.notification.NotificationService;
import com.myicpc.service.timeline.TimelineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Roman Smetana
 */
@Controller
public class TimelineController extends GeneralController {

    @Autowired
    private TimelineService timelineService;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = {"/{contestCode}", "/{contestCode}/timeline"}, method = RequestMethod.GET)
    public String timeline(Model model, @PathVariable String contestCode, HttpServletRequest request,
                           HttpServletResponse response) {
        Contest contest = getContest(contestCode, model);

        List<Notification> timelineNotifications = timelineService.getTimelineNotifications(contest);

        List<Notification> questNotifications = notificationService.getFeaturedQuestNotifications(NotificationService.getFeaturedIdsFromCookie(request, response));

        model.addAttribute("notifications", timelineNotifications);
        model.addAttribute("availableNotificationTypes", TimelineService.TIMELINE_TYPES);
        model.addAttribute("openQuests", questNotifications);
        return "timeline/timeline";
    }

    @RequestMapping(value = {"/{contestCode}/empty"}, method = RequestMethod.GET)
    public String empty(Model model, @PathVariable String contestCode) {
        Contest contest = getContest(contestCode, model);

        return "empty";
    }
}
