package com.myicpc.controller.admin;

import com.myicpc.controller.GeneralAdminController;
import com.myicpc.enums.ContestParticipantRole;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.teamInfo.ContestParticipant;
import com.myicpc.model.teamInfo.TeamInfo;
import com.myicpc.repository.teamInfo.ContestParticipantRepository;
import com.myicpc.repository.teamInfo.TeamInfoRepository;
import com.myicpc.service.dto.filter.ParticipantFilterDTO;
import com.myicpc.service.exception.BusinessValidationException;
import com.myicpc.service.participant.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;


/**
 * Controller for {@link ContestParticipant} management
 *
 * It provides the listing and CRUD features for {@link ContestParticipant}s
 *
 * @author Roman Smetana
 */
@Controller
@SessionAttributes({"participantFilter", "participant"})
public class ParticipantsAdminController extends GeneralAdminController {
    @Autowired
    private ParticipantService participantService;

    @Autowired
    private TeamInfoRepository teamInfoRepository;

    @Autowired
    private ContestParticipantRepository contestParticipantRepository;

    @ModelAttribute("participantFilter")
    public ParticipantFilterDTO createParticipantFilter() {
        return new ParticipantFilterDTO();
    }

    /**
     * Shows contest participants administration page
     *
     * @param model
     * @return view
     */
    @RequestMapping(value = "/private/{contestCode}/participants", method = RequestMethod.GET)
    public String participants(@PathVariable String contestCode, @ModelAttribute("participantFilter") ParticipantFilterDTO participantFilter, Model model) {
        Contest contest = getContest(contestCode, model);
        model.addAttribute("newParticipant", new ContestParticipant());

        List<TeamInfo> teamInfos = participantService.getTeamInfosSortedByName(contest);

        model.addAttribute("participantRoles", ContestParticipantRole.getAllTeamRoles());
        model.addAttribute("participantFilter", participantFilter);
        model.addAttribute("teamInfos", teamInfos);
        if (contest.isShowTeamNames()) {
            model.addAttribute("teams", teamInfoRepository.findByContestOrderByNameAsc(contest));
        } else {
            model.addAttribute("teams", teamInfoRepository.findByContestOrderByUniversityNameAsc(contest));
        }
        model.addAttribute("allPeople", contestParticipantRepository.findByContestOrderByName(contest));
        model.addAttribute("contestants", contestParticipantRepository.findByContestParticipantRoleAndContestOrderByName(ContestParticipantRole.CONTESTANT, contest));
        model.addAttribute("coaches", contestParticipantRepository.findByContestParticipantRoleAndContestOrderByName(ContestParticipantRole.COACH, contest));
        model.addAttribute("attendees", contestParticipantRepository.findByContestParticipantRoleAndContestOrderByName(ContestParticipantRole.ATTENDEE, contest));
        model.addAttribute("staff", contestParticipantRepository.findByContestParticipantRoleAndContestOrderByName(ContestParticipantRole.STAFF, contest));

        return "private/participants/participantList";
    }

    @RequestMapping(value = "/private/{contestCode}/participants/create", method = RequestMethod.POST)
    public String createParticipant(@PathVariable String contestCode, @ModelAttribute("newParticipant") ContestParticipant newParticipant,
                                    @RequestParam String participantRole, @RequestParam(required = false) Long teamInfoId, RedirectAttributes redirectAttributes) {
        Contest contest = getContest(contestCode, null);
        try {
            participantService.createContestParticipant(newParticipant, contest, participantRole, teamInfoId);

            successMessage(redirectAttributes, "participantAdmin.create.success", newParticipant.getOfficialFullname());
        } catch (ValidationException ex) {
            errorMessage(redirectAttributes, ex);
        }
        return "redirect:/private/" + getContestURL(contestCode) + "/participants";
    }

    @RequestMapping(value = "/private/{contestCode}/participant/{participantId}/edit", method = RequestMethod.GET)
    public String editParticipant(@PathVariable String contestCode, @PathVariable Long participantId, Model model) {
        getContest(contestCode, model);

        ContestParticipant participant = contestParticipantRepository.findOne(participantId);

        model.addAttribute("participant", participant);

        return "private/participants/editParticipant";
    }

    @RequestMapping(value = "/private/{contestCode}/participant/{participantId}/edit", method = RequestMethod.POST)
    public String editParticipantPOST(@PathVariable String contestCode, @Valid @ModelAttribute("participant") ContestParticipant contestParticipant, final BindingResult result,
                                      final Model model, final RedirectAttributes redirectAttributes) {
        getContest(contestCode, model);

        if (result.hasErrors()) {
            return "private/participants/editParticipant";
        }

        try {
            participantService.saveParticipant(contestParticipant);
            successMessage(redirectAttributes, "save.success");
        } catch (BusinessValidationException e) {
            model.addAttribute("errorMsg", getMessage(e.getMessageCode()));
            return "private/participants/editParticipant";
        }
        return "redirect:/private/" + getContestURL(contestCode) + "/participants";
    }
}
