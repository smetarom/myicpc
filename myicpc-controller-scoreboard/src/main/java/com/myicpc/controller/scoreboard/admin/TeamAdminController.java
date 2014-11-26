package com.myicpc.controller.scoreboard.admin;

import com.myicpc.controller.GeneralAdminController;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.teamInfo.TeamInfo;
import com.myicpc.repository.teamInfo.ContestParticipantRepository;
import com.myicpc.repository.teamInfo.TeamInfoRepository;
import com.myicpc.service.exception.WebServiceException;
import com.myicpc.service.scoreboard.team.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.ValidationException;
import java.io.IOException;

/**
 * @author Roman Smetana
 */
@Controller
public class TeamAdminController extends GeneralAdminController {
    @Autowired
    private TeamService teamService;

    @Autowired
    private ContestParticipantRepository contestParticipantRepository;

    @Autowired
    private TeamInfoRepository teamInfoRepository;

    /**
     * Shows team administration
     *
     * @param model
     * @return view
     */
    @RequestMapping(value = "/private/{contestCode}/teams", method = RequestMethod.GET)
    public String teams(@PathVariable String contestCode, Model model) {
        Contest contest = getContest(contestCode, model);
        Iterable<TeamInfo> teamInfos = teamInfoRepository.findByContest(contest);

        model.addAttribute("teamInfos", teamInfos);
        model.addAttribute("active", "List");
        return "private/teams/teamsHome";
    }

    /**
     * Shows team synchronization page
     *
     * @param model
     * @return view
     */
    @RequestMapping(value = "/private/{contestCode}/teams/synchronize", method = RequestMethod.GET)
    public String teamsSync(@PathVariable String contestCode, Model model) {
        Contest contest = getContest(contestCode, model);
        model.addAttribute("active", "Sync");
        model.addAttribute("breadcrumb", getMessage("teamAdmin.sync"));
        model.addAttribute("warnMsg", getMessage("teamAdmin.sync.message") + " " + getMessage("teamAdmin.sync.message2"));
        return "private/teams/teamsHome";
    }

    @RequestMapping(value = "/private/{contestCode}/teams/synchronize", method = RequestMethod.POST)
    public String processTeamSync(@PathVariable String contestCode, RedirectAttributes redirectAttributes) throws WebServiceException {
        Contest contest = getContest(contestCode, null);
        try {
            teamService.synchronizeTeamsWithCM(contest);
            successMessage(redirectAttributes, "teamAdmin.sync.success");
        } catch (ValidationException ex) {
            errorMessage(redirectAttributes, ex);
        }
        return "redirect:/private/" + contestCode + "/teams/synchronize";
    }

    @RequestMapping(value = "/private/{contestCode}/teams/synchronize/staff-members", method = RequestMethod.POST)
    public String processStaffSync(@PathVariable String contestCode, RedirectAttributes redirectAttributes) throws WebServiceException {
        Contest contest = getContest(contestCode, null);
        try {
            teamService.synchronizeStaffMembersWithCM(contest);
            successMessage(redirectAttributes, "teamAdmin.sync.success");
        } catch (ValidationException ex) {
            errorMessage(redirectAttributes, ex);
        }

        return "redirect:/private/" + contestCode + "/teams/synchronize";
    }

    @RequestMapping(value = "/private/{contestCode}/teams/synchronize/social-content", method = RequestMethod.POST)
    public String processSocialContentSync(@PathVariable String contestCode, Model model, RedirectAttributes redirectAttributes) throws WebServiceException {
        Contest contest = getContest(contestCode, null);
        teamService.synchronizeSocialInfosWithCM(contest);

        successMessage(redirectAttributes, "teamAdmin.sync.success");
        redirectAttributes.addFlashAttribute("active", "sync");
        return "redirect:/private/" + contestCode + "/teams/synchronize";
    }

    @RequestMapping(value = "/private/{contestCode}/teams/synchronize-manual", method = RequestMethod.POST)
    public String processTeamSyncManually(@PathVariable String contestCode, @RequestParam("universityJSON") MultipartFile universityJSON,
                                          @RequestParam("teamJSON") MultipartFile teamJSON, RedirectAttributes redirectAttributes) throws IOException {
        Contest contest = getContest(contestCode, null);
        try {
            teamService.synchronizeTeamsFromFile(universityJSON, teamJSON, contest);
            successMessage(redirectAttributes, "teamAdmin.sync.success");
        } catch (ValidationException ex) {
            errorMessage(redirectAttributes, ex);
        }

        return "redirect:/private/" + contestCode + "/teams/synchronize";
    }

    @RequestMapping(value = "/private/{contestCode}/teams/contestteamidmapping", method = RequestMethod.POST)
    public String processTeamContestIdsMapping(@PathVariable String contestCode, @RequestParam("teamContestJSON") MultipartFile teamContestJSON,
                                               RedirectAttributes redirectAttributes) {
        Contest contest = getContest(contestCode, null);
        try {
            //TODO
//          teamService.uploadTeamContestIdsMappingFile(teamContestJSON, contest);
            successMessage(redirectAttributes, "teamAdmin.teamContestIds.success");
        } catch (ValidationException ex) {
            errorMessage(redirectAttributes, ex);
        }

        return "redirect:/private/" + contestCode + "/teams/synchronize";
    }

    @RequestMapping(value = "/private/{contestCode}/teams/abbreviation", method = RequestMethod.GET)
    public String teamsAbbr(@PathVariable String contestCode, Model model) {
        Contest contest = getContest(contestCode, model);
        model.addAttribute("active", "Abbreviation");
        model.addAttribute("breadcrumb", getMessage("teamAdmin.abbr"));
        model.addAttribute("warnMsg", getMessage("teamAdmin.abbr.message"));
        return "private/teams/teamsHome";
    }

    @RequestMapping(value = "/private/{contestCode}/teams/hashtags", method = RequestMethod.GET)
    public String teamsHashtags(@PathVariable String contestCode, Model model) {
        Contest contest = getContest(contestCode, model);
        model.addAttribute("active", "Hashtags");
        model.addAttribute("breadcrumb", getMessage("teamAdmin.hashtag"));
        model.addAttribute("warnMsg", getMessage("teamAdmin.hashtags.message"));
        return "private/teams/teamsHome";
    }

    @RequestMapping(value = "/private/{contestCode}/teams/abbreviation", method = RequestMethod.POST)
    public String processTeamAbbr(@PathVariable String contestCode, RedirectAttributes redirectAttributes, @RequestParam("file") MultipartFile file) {
        Contest contest = getContest(contestCode, null);
        try {
            teamService.uploadAbbreviationFile(file, contest);
            successMessage(redirectAttributes, "teamAdmin.abbr.success");
        } catch (ValidationException ex) {
            errorMessage(redirectAttributes, ex);
        }

        return "redirect:/private/" + contestCode + "/teams/abbreviation";
    }

    @RequestMapping(value = "/private/{contestCode}/teams/hashtags", method = RequestMethod.POST)
    public String processTeamHashtags(@PathVariable String contestCode, RedirectAttributes redirectAttributes,
                                      @RequestParam("file") MultipartFile file) {
        Contest contest = getContest(contestCode, null);
        try {
            teamService.uploadHashtagsFile(file, contest);
            successMessage(redirectAttributes, "teamAdmin.hashtags.success");
        } catch (ValidationException ex) {
            errorMessage(redirectAttributes, ex);
        }

        return "redirect:/private/" + contestCode + "/teams/hashtags";
    }
}
