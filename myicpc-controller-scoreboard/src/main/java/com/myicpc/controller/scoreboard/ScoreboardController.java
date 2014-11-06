package com.myicpc.controller.scoreboard;

import com.myicpc.controller.GeneralAbstractController;
import com.myicpc.controller.GeneralController;
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
    @RequestMapping(value = {"/{contestCode}/scoreboard"}, method = RequestMethod.GET)
    public String scoreboard(@PathVariable String contestCode, Model model, HttpSession session, HttpServletRequest request, @CookieValue(value = "followedTeams", required = false) String followedTeams) {

        return "scoreboard/scoreboard";
    }
}
