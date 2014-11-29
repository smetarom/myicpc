package com.myicpc.controller.scoreboard;

import com.myicpc.controller.GeneralAbstractController;
import com.myicpc.controller.GeneralController;
import com.myicpc.model.contest.Contest;
import com.myicpc.service.scoreboard.team.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Roman Smetana
 */
@Controller
public class ScoreboardController extends GeneralController {

    @Autowired
    private TeamService teamService;

    @RequestMapping(value = {"/{contestCode}/scoreboard"}, method = RequestMethod.GET)
    public String scoreboard(@PathVariable String contestCode, Model model, HttpSession session, HttpServletRequest request, @CookieValue(value = "followedTeams", required = false) String followedTeams) {
        Contest contest = getContest(contestCode, model);

        model.addAttribute("scoreboardAvailable", true);
        model.addAttribute("sideMenuActive", "scoreboard");
        return "scoreboard/scoreboard";
    }
}
