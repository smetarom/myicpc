package com.myicpc.controller.admin;

import com.myicpc.controller.GeneralAdminController;
import com.myicpc.enums.ContestParticipantRole;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.teamInfo.ContestParticipant;
import com.myicpc.repository.teamInfo.ContestParticipantRepository;
import com.myicpc.repository.teamInfo.TeamInfoRepository;
import com.myicpc.service.settings.GlobalSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Roman Smetana
 */
@Controller
public class ParticipantsAdminController extends GeneralAdminController {
    @Autowired
    private GlobalSettingsService globalSettingsService;

    @Autowired
    private TeamInfoRepository teamInfoRepository;

    @Autowired
    private ContestParticipantRepository contestParticipantRepository;

    /**
     * Shows contest participants administration page
     *
     * @param model
     * @return view
     */
    @RequestMapping(value = "/private/{contestCode}/participants", method = RequestMethod.GET)
    public String participants(@PathVariable String contestCode, Model model) {
        Contest contest = getContest(contestCode, model);
        model.addAttribute("newParticipant", new ContestParticipant());

        model.addAttribute("teamRoles", ContestParticipantRole.getAllTeamRoles());
        if (contest.getContestSettings().isShowTeamNames()) {
            model.addAttribute("teams", teamInfoRepository.findAllOrderByName());
        } else {
            model.addAttribute("teams", teamInfoRepository.findAllOrderByUniversityName());
        }
        model.addAttribute("allPeople", contestParticipantRepository.findAllOrderByName());
        model.addAttribute("contestants", contestParticipantRepository.findByContestParticipantRoleOrderByName(ContestParticipantRole.CONTESTANT));
        model.addAttribute("coaches", contestParticipantRepository.findByContestParticipantRoleOrderByName(ContestParticipantRole.COACH));
        model.addAttribute("attendees", contestParticipantRepository.findByContestParticipantRoleOrderByName(ContestParticipantRole.ATTENDEE));
        model.addAttribute("staff", contestParticipantRepository.findByContestParticipantRoleOrderByName(ContestParticipantRole.STAFF));

        return "private/participants/participantList";
    }
}
