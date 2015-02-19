package com.myicpc.controller;

import com.myicpc.enums.NotificationType;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.social.Notification;
import com.myicpc.service.contest.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

/**
 * @author Roman Smetana
 */
@Controller
public class HomeController extends GeneralController {
    @Autowired
    private ContestService contestService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String timeline(Model model) {

        model.addAttribute("contests", contestService.getActiveContests());
        return "home";
    }
}
