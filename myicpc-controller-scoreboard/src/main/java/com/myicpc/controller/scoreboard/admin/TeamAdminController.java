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
 * Controller for team management
 * <p/>
 * It provides the import team support
 *
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
        model.addAttribute("breadcrumb", getMessage("teamAdmin.sync"));
        model.addAttribute("warnMsg", getMessage("teamAdmin.sync.message") + " " + getMessage("teamAdmin.sync.message2"));
        return "private/teams/teamsSynchronize";
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
                                          @RequestParam("regionJSON") MultipartFile regionJSON, @RequestParam("teamJSON") MultipartFile teamJSON,
                                          RedirectAttributes redirectAttributes) throws IOException {
        Contest contest = getContest(contestCode, null);
        try {
            teamService.synchronizeTeamsFromFile(universityJSON, regionJSON, teamJSON, contest);
            successMessage(redirectAttributes, "teamAdmin.sync.success");
        } catch (ValidationException ex) {
            errorMessage(redirectAttributes, ex);
        }

        return "redirect:/private/" + contestCode + "/teams/synchronize";
    }
}
