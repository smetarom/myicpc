package com.myicpc.controller.scoreboard;

import com.myicpc.controller.GeneralAbstractController;
import com.myicpc.controller.GeneralController;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Problem;
import com.myicpc.repository.eventFeed.ProblemRepository;
import com.myicpc.repository.eventFeed.TeamRepository;
import com.myicpc.service.scoreboard.ScoreboardService;
import com.myicpc.service.scoreboard.problem.ProblemService;
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
import java.util.List;

/**
 * @author Roman Smetana
 */
@Controller
public class ScoreboardController extends GeneralController {

    @Autowired
    private TeamService teamService;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ScoreboardService scoreboardService;

    @Autowired
    private ProblemService problemService;

    @Autowired
    private ProblemRepository problemRepository;

    @RequestMapping(value = {"/{contestCode}/scoreboard"}, method = RequestMethod.GET)
    public String scoreboard(@PathVariable String contestCode, Model model, HttpSession session, HttpServletRequest request, @CookieValue(value = "followedTeams", required = false) String followedTeams) {
        Contest contest = getContest(contestCode, model);

        List<Problem> problems = problemService.findByContest(contest);

        model.addAttribute("teamJSON", scoreboardService.getTeamsFullTemplate(contest).toString());
        model.addAttribute("problems", problems);
        model.addAttribute("numProblems", problems.size());
        model.addAttribute("scoreboardAvailable", true);
        model.addAttribute("sideMenuActive", "scoreboard");
        return "scoreboard/scoreboard";
    }

    @RequestMapping(value = {"/{contestCode}/scorebar"}, method = RequestMethod.GET)
    public String scoreboard(@PathVariable String contestCode, Model model) {
        Contest contest = getContest(contestCode, model);

        model.addAttribute("teamJSON", scoreboardService.getTeamsScorebarTemplate(contest).toString());
        model.addAttribute("problemCount", problemRepository.countByContest(contest));
        model.addAttribute("teamCount", teamRepository.countByContest(contest));
        return "scoreboard/scorebar";
    }

    @RequestMapping(value = {"/{contestCode}/map"}, method = RequestMethod.GET)
    public String map(@PathVariable String contestCode, Model model) {
        Contest contest = getContest(contestCode, model);

        model.addAttribute("teamJSON", scoreboardService.getTeamsScorebarTemplate(contest).toString());
        model.addAttribute("teamCoordinatesJSON", scoreboardService.getTeamMapCoordinates(contest).toString());
        model.addAttribute("problemCount", problemRepository.countByContest(contest));
        model.addAttribute("teamCount", teamRepository.countByContest(contest));
        return "scoreboard/worldMap";
    }
}
