package com.myicpc.controller;

import com.google.gson.JsonObject;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.social.Notification;
import com.myicpc.service.dto.TimelineFeaturedNotificationsDTO;
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
 * Controller for timeline
 *
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
            model.addAttribute("lastTimelineId", timelineNotifications.get(timelineNotifications.size() - 1).getId());
        }

        TimelineFeaturedNotificationsDTO featuredNotifications = notificationService.getTimelineFeaturedNotifications(NotificationService.getFeaturedIdsFromCookie(request, response), contest);

        model.addAttribute("notifications", timelineNotifications);
        model.addAttribute("availableNotificationTypes", TimelineService.TIMELINE_TYPES);
        model.addAttribute("openQuests", NotificationService.getNotificationInJson(featuredNotifications.getQuestNotifications()));
        model.addAttribute("openPolls", NotificationService.getNotificationInJson(featuredNotifications.getPollNotifications()));
        model.addAttribute("openAdminNotifications", NotificationService.getNotificationInJson(featuredNotifications.getAdminNotifications()));
        model.addAttribute("editable", false);
        return "timeline/timeline";
    }

    @RequestMapping(value = "/private/{contestCode}/timeline", method = RequestMethod.GET)
    public String adminTimeline(Model model, @PathVariable String contestCode, HttpServletRequest request,
                           HttpServletResponse response) {
        Contest contest = getContest(contestCode, model);

        List<Notification> timelineNotifications = timelineService.getTimelineNotifications(contest);
        if (timelineNotifications != null && !timelineNotifications.isEmpty()) {
            model.addAttribute("lastTimelineId", timelineNotifications.get(timelineNotifications.size() - 1).getId());
        }

        model.addAttribute("notifications", timelineNotifications);
        model.addAttribute("availableNotificationTypes", TimelineService.TIMELINE_TYPES);
        model.addAttribute("editable", true);
        return "timeline/adminTimeline";
    }

    @RequestMapping(value = "/{contestCode}/timeline/loadMore", method = RequestMethod.GET)
    @ResponseBody
    public String timelineLoadMore(@RequestParam Long lastTimelineId, @PathVariable String contestCode,Model model, HttpServletRequest request, HttpSession session) {
        Contest contest = getContest(contestCode, model);
        if (lastTimelineId != null) {
            JsonObject o = new JsonObject();
            List<Notification> notifications = timelineService.getTimelineNotifications(lastTimelineId, contest);
            if (notifications != null && !notifications.isEmpty()) {
                o.addProperty("lastTimelineId", notifications.get(notifications.size() - 1).getId());
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
