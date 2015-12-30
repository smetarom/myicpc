package com.myicpc.controller;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.teamInfo.ContestParticipant;
import com.myicpc.model.teamInfo.ContestParticipantAssociation;
import com.myicpc.repository.quest.QuestSubmissionRepository;
import com.myicpc.repository.teamInfo.AttendedContestRepository;
import com.myicpc.repository.teamInfo.ContestParticipantAssociationRepository;
import com.myicpc.repository.teamInfo.ContestParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller for public pages about {@link ContestParticipant}
 *
 * @author Roman Smetana
 */
@Controller
public class PeopleController extends GeneralController {

    @Autowired
    private ContestParticipantRepository contestParticipantRepository;

    @Autowired
    private AttendedContestRepository attendedContestRepository;

    @Autowired
    private ContestParticipantAssociationRepository contestParticipantAssociationRepository;

    @Autowired
    private QuestSubmissionRepository questSubmissionRepository;

    @RequestMapping(value = "/{contestCode}/people/{personId}", method = RequestMethod.GET)
    public String personDetail(Model model, @PathVariable String contestCode, @PathVariable Long personId, RedirectAttributes redirectAttributes) {
        Contest contest = getContest(contestCode, model);

        ContestParticipant contestParticipant = contestParticipantRepository.findOne(personId);
        if (contestParticipant == null) {
            errorMessage(redirectAttributes, "person.noResult");
            return "redirect:" + getContestURL(contestCode);
        }

        model.addAttribute("contestParticipant", contestParticipant);
        List<ContestParticipantAssociation> associations = contestParticipantAssociationRepository.findByContestParticipantAndContest(contestParticipant, contest);
        model.addAttribute("associations", associations);
        model.addAttribute("questSubmissions", questSubmissionRepository.findByContestParticipant(contestParticipant));
        model.addAttribute("attendedContests", attendedContestRepository.findByContestParticipantOrderByYearDesc(contestParticipant));
        return "people/personDetail";
    }
}
