package com.myicpc.controller.scoreboard;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.myicpc.controller.GeneralController;
import com.myicpc.enums.ContestParticipantRole;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Problem;
import com.myicpc.model.eventFeed.Team;
import com.myicpc.model.eventFeed.TeamProblem;
import com.myicpc.model.teamInfo.ContestParticipant;
import com.myicpc.model.teamInfo.TeamInfo;
import com.myicpc.repository.eventFeed.ProblemRepository;
import com.myicpc.repository.eventFeed.TeamProblemRepository;
import com.myicpc.repository.eventFeed.TeamRepository;
import com.myicpc.repository.teamInfo.ContestParticipantRepository;
import com.myicpc.repository.teamInfo.TeamInfoRepository;
import com.myicpc.service.scoreboard.dto.SubmissionDTO;
import com.myicpc.service.scoreboard.insight.ProblemInsightService;
import com.myicpc.service.scoreboard.team.TeamService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.site.SitePreference;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Collections;
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
    private TeamInfoRepository teamInfoRepository;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private TeamProblemRepository teamProblemRepository;

    @Autowired
    private ProblemInsightService problemInsightService;

    @Autowired
    private ContestParticipantRepository contestParticipantRepository;

    @RequestMapping(value = "/{contestCode}/teams", method = RequestMethod.GET)
    public String teams(@PathVariable String contestCode, @RequestParam(required = false, defaultValue = "grid") String view,
                        Model model, SitePreference sitePreference) {
        Contest contest = getContest(contestCode, model);

        List<TeamInfo> teamInfos = teamService.getTeamInfosByContest(contest);

        model.addAttribute("teamInfos", teamInfos);
        model.addAttribute("sideMenuActive", "scoreboard");

        if (sitePreference.isMobile()) {
            model.addAttribute("view", "list");
        } else {
            model.addAttribute("showSwitch", true);
            model.addAttribute("view", view);
        }

        return "scoreboard/teams";
    }

    @RequestMapping(value = { "/{contestCode}/team/{teamId}", "/{contestCode}/team/{teamId}/contest" }, method = RequestMethod.GET)
    public String teamContest(@PathVariable String contestCode, @PathVariable Long teamId, Model model) {
        Team team = teamRepository.findByExternalId(teamId);
        if (team == null) {
            return "redirect:" + getContestURL(contestCode) + "/team/" + teamId + "/profile";
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

    @RequestMapping(value = {"/{contestCode}/team/{teamId}/profile"}, method = RequestMethod.GET)
    public String teamProfile(@PathVariable String contestCode, @PathVariable Long teamId, Model model) {
        Contest contest = getContest(contestCode, model);
        TeamInfo teamInfo = teamInfoRepository.findByExternalIdAndContestWithRegionalResult(teamId, contest);
        if (teamInfo == null) {
            // TODO team not found
        }
        Team team = teamRepository.findByExternalId(teamId);
        List<ContestParticipant> coaches = contestParticipantRepository.findByTeamInfoAndContestParticipantRole(teamInfo, ContestParticipantRole.COACH);
        List<ContestParticipant> contestants = contestParticipantRepository.findByTeamInfoAndContestParticipantRole(teamInfo, ContestParticipantRole.CONTESTANT);
        List<ContestParticipant> reserves = contestParticipantRepository.findByTeamInfoAndContestParticipantRole(teamInfo, ContestParticipantRole.RESERVE);
        List<ContestParticipant> attendees = contestParticipantRepository.findByTeamInfoAndContestParticipantRole(teamInfo, ContestParticipantRole.ATTENDEE);

        List<ContestParticipant> peopleInCarousel = Lists.newArrayList();
        peopleInCarousel.addAll(coaches);
        peopleInCarousel.addAll(contestants);

        model.addAttribute("teamInfo", teamInfo);
        model.addAttribute("team", team);

        model.addAttribute("coaches", coaches);
        model.addAttribute("contestants", contestants);
        model.addAttribute("reserves", reserves);
        model.addAttribute("attendees", attendees);
        model.addAttribute("peopleInCarousel", peopleInCarousel);

        model.addAttribute("tab", "profile");
        return "scoreboard/teamProfile";
    }

    @RequestMapping(value = {"/{contestCode}/team/{teamId}/insight"}, method = RequestMethod.GET)
    public String teamInsight(@PathVariable String contestCode, @PathVariable Long teamId, Model model) {
        Team team = teamRepository.findByExternalId(teamId);
        Contest contest = getContest(contestCode, model);

        model.addAttribute("team", team);
        model.addAttribute("insightProblemModel", problemInsightService.reportAll(team, contest).toString());
        model.addAttribute("tab", "insight");

        return "scoreboard/teamInsight";
    }

    @RequestMapping(value = {"/{contestCode}/team/{teamId}/insight/template/team-problems"}, method = RequestMethod.GET)
    public String teamInsightTemplate(@PathVariable String contestCode, @PathVariable Long teamId, Model model) {
        Team team = teamRepository.findByExternalId(teamId);
        Contest contest = getContest(contestCode, model);

        model.addAttribute("team", team);
        model.addAttribute("isTeamInsight", true);

        return "scoreboard/insight/template/allProblems";
    }

    @RequestMapping(value = {"/{contestCode}/team/{teamId}/social"}, method = RequestMethod.GET)
    public String teamSocial(@PathVariable String contestCode, @PathVariable Long teamId, Model model) {
        TeamInfo teamInfo = teamInfoRepository.findByExternalId(teamId);
        if (teamInfo == null) {
            // TODO team not found
        }
        Contest contest = getContest(contestCode, model);
        Team team = teamRepository.findByExternalId(teamId);

        model.addAttribute("teamInfo", teamInfo);
        model.addAttribute("team", team);

        model.addAttribute("tab", "social");
        return "scoreboard/teamSocial";
    }
}
