package com.myicpc.controller;

import com.myicpc.enums.NotificationType;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.social.Notification;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

/**
 * @author Roman Smetana
 */
public class HomeController extends GeneralController {

    @RequestMapping(value = {"/", "/timeline", "/private/timeline"}, method = RequestMethod.GET)
    public String timeline(Model model) {
        Contest contest = getContest("CTU-Open-2013", model);
        Notification notification = new Notification();
        notification.setTitle("ahoj");
        notification.setBody("<i>italic</i>");
        notification.setTimestamp(new Date());
        notification.setContest(contest);
        notification.setNotificationType(NotificationType.SCOREBOARD_SUCCESS);
        model.addAttribute("notification", notification);
        return "home";
    }
}
