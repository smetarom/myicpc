package com.myicpc.controller.scoreboard.admin;

import com.myicpc.controller.GeneralAdminController;
import com.myicpc.model.contest.Contest;
import com.myicpc.service.scoreboard.eventFeed.ControlFeedService;
import com.myicpc.service.scoreboard.exception.EventFeedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Roman Smetana
 */
@Controller
public class ScoreboardAdminController extends GeneralAdminController {

    @Autowired
    private ControlFeedService controlFeedService;

    @RequestMapping(value = "/private/{contestCode}/scoreboard/feed/start", method = RequestMethod.GET)
    public String startFeed(@PathVariable String contestCode) throws EventFeedException {
        Contest contest = getContest(contestCode, null);

        controlFeedService.startFeed(contest);

        return "redirect:/private"+getContestURL(contestCode)+"/home";
    }
}
