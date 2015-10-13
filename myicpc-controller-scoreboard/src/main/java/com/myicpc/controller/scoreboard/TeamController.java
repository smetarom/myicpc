package com.myicpc.controller.scoreboard;

import com.google.common.collect.Lists;
import com.myicpc.controller.GeneralController;
import com.myicpc.enums.ContestParticipantRole;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Team;
import com.myicpc.model.eventFeed.TeamProblem;
import com.myicpc.model.teamInfo.ContestParticipant;
import com.myicpc.model.teamInfo.TeamInfo;
import com.myicpc.repository.eventFeed.ProblemRepository;
import com.myicpc.repository.eventFeed.TeamProblemRepository;
import com.myicpc.repository.eventFeed.TeamRepository;
import com.myicpc.repository.teamInfo.ContestParticipantRepository;
import com.myicpc.repository.teamInfo.TeamInfoRepository;
import com.myicpc.service.dto.GlobalSettings;
import com.myicpc.service.scoreboard.dto.SubmissionDTO;
import com.myicpc.service.scoreboard.insight.ProblemInsightService;
import com.myicpc.service.scoreboard.team.TeamService;
import com.myicpc.service.settings.GlobalSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.site.SitePreference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller for public team pages
 *
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

    @Autowired
    private GlobalSettingsService globalSettingsService;

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
            GlobalSettings globalSettings = globalSettingsService.getGlobalSettings();
            model.addAttribute("showSwitch", true);
            model.addAttribute("teamPictureURLPrefix", globalSettings.getTeamPicturesUrl());
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

        model.addAttribute("team", team);
        model.addAttribute("teamInfo", team.getTeamInfo());
        model.addAttribute("problems", problemRepository.findByContestOrderByCodeAsc(contest));
        model.addAttribute("timeline", timeline);
        model.addAttribute("submissions", submissions);
        model.addAttribute("tab", "contest");
        return "scoreboard/teamContest";
    }

    @RequestMapping(value = {"/{contestCode}/team/{teamId}/profile"}, method = RequestMethod.GET)
    public String teamProfile(@PathVariable String contestCode, @PathVariable Long teamId, Model model, RedirectAttributes redirectAttributes) {
        Contest contest = getContest(contestCode, model);
        TeamInfo teamInfo = teamInfoRepository.findByExternalIdAndContestWithRegionalResult(teamId, contest);
        if (teamInfo == null) {
            errorMessage(redirectAttributes, "team.notFound");
            return "redirect:" + getContestURL(contestCode) + "/teams";
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
        model.addAttribute("teamInfo", team.getTeamInfo());
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
    public String teamSocial(@PathVariable String contestCode, @PathVariable Long teamId, Model model, RedirectAttributes redirectAttributes) {
        TeamInfo teamInfo = teamInfoRepository.findByExternalId(teamId);
        if (teamInfo == null) {
            errorMessage(redirectAttributes, "team.notFound");
            return "redirect:" + getContestURL(contestCode) + "/teams";
        }
        Contest contest = getContest(contestCode, model);
        Team team = teamRepository.findByExternalId(teamId);

        model.addAttribute("teamInfo", teamInfo);
        model.addAttribute("team", team);

        model.addAttribute("tab", "social");
        return "scoreboard/teamSocial";
    }
}
