package com.myicpc.controller;

import com.google.gson.JsonObject;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.social.Notification;
import com.myicpc.service.notification.NotificationService;
import com.myicpc.service.timeline.TimelineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
        if (timelineNotifications != null && !timelineNotifications.isEmpty()) {
            model.addAttribute("lastTimelineId", timelineNotifications.get(timelineNotifications.size() - 1).getTimestamp().getTime());
        }

        List<Notification> questNotifications = notificationService.getFeaturedQuestNotifications(NotificationService.getFeaturedIdsFromCookie(request, response));

        model.addAttribute("notifications", timelineNotifications);
        model.addAttribute("availableNotificationTypes", TimelineService.TIMELINE_TYPES);
        model.addAttribute("openQuests", questNotifications);
        return "timeline/timeline";
    }

    @RequestMapping(value = "/{contestCode}/timeline/loadMore", method = RequestMethod.GET)
    @ResponseBody
    public String timelineLoadMore(@RequestParam Long lastTimestamp, @PathVariable String contestCode,Model model, HttpServletRequest request, HttpSession session) {
        Contest contest = getContest(contestCode, model);
        if (lastTimestamp != null) {
            JsonObject o = new JsonObject();
            List<Notification> notifications = timelineService.getTimelineNotifications(lastTimestamp, contest);
            if (notifications != null && !notifications.isEmpty()) {
                o.addProperty("lastTimelineId", notifications.get(notifications.size() - 1).getTimestamp().getTime());
            }
            o.add("data", NotificationService.getNotificationsInJson(notifications));
            return o.toString();
        }
        return new JsonObject().toString();
    }

    @RequestMapping(value = {"/{contestCode}/empty"}, method = RequestMethod.GET)
    public String empty(Model model, @PathVariable String contestCode) {
        Contest contest = getContest(contestCode, model);

        return "empty";
    }
}
