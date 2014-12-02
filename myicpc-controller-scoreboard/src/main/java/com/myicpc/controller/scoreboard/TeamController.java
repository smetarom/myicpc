package com.myicpc.controller.scoreboard;

import com.google.gson.JsonArray;
import com.myicpc.controller.GeneralController;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Problem;
import com.myicpc.model.eventFeed.Team;
import com.myicpc.model.eventFeed.TeamProblem;
import com.myicpc.repository.eventFeed.ProblemRepository;
import com.myicpc.repository.eventFeed.TeamProblemRepository;
import com.myicpc.repository.eventFeed.TeamRepository;
import com.myicpc.service.scoreboard.dto.SubmissionDTO;
import com.myicpc.service.scoreboard.team.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author Roman Smetana
 */
@Controller
public class TeamController extends GeneralController {
    @Autowired
    private TeamService teamService;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private TeamProblemRepository teamProblemRepository;

    @RequestMapping(value = { "/{contestCode}/team/{teamId}", "/{contestCode}/team/{teamId}/contest" }, method = RequestMethod.GET)
    public String teamContest(@PathVariable String contestCode, @PathVariable Long teamId, Model model) {
        Team team = teamRepository.findOne(teamId);
        if (team == null) {
            return "redirect:" + getContestURL(contestCode) + "/team/" + teamId + "/about";
        }
        team.setCurrentProblems(teamService.getLatestTeamProblems(team));
        Contest contest = getContest(contestCode, model);

        List<TeamProblem> submissions = teamProblemRepository.findByTeamOrderByTimeDesc(team);
        List<SubmissionDTO> timeline = teamService.getTeamSubmissionDTOs(team);
        JsonArray rankHistoryChartData = teamService.getRankHistoryChartData(team);

        model.addAttribute("team", team);
        model.addAttribute("problems", problemRepository.findByContestOrderByCodeAsc(contest));
        model.addAttribute("timeline", timeline);
        model.addAttribute("submissions", submissions);
        model.addAttribute("rankHistoryJSON", rankHistoryChartData);
        model.addAttribute("tab", "contest");
        return "scoreboard/teamContest";
    }

    @RequestMapping(value = {"/{contestCode}/team/{teamId}/about"}, method = RequestMethod.GET)
    public String teamAbout(@PathVariable String contestCode, @PathVariable Long teamId, Model model) {
        return "scoreboard/teamAbout";
    }
}
