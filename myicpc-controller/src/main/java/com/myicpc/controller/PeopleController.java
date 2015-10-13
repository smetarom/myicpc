package com.myicpc.controller;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.teamInfo.ContestParticipant;
import com.myicpc.repository.teamInfo.ContestParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller for public pages about {@link ContestParticipant}
 *
 * @author Roman Smetana
 */
@Controller
public class PeopleController extends GeneralController {

    @Autowired
    private ContestParticipantRepository contestParticipantRepository;

    @RequestMapping(value = "/{contestCode}/people/{personId}", method = RequestMethod.GET)
    public String timeline(Model model, @PathVariable String contestCode, @PathVariable Long personId) {
        Contest contest = getContest(contestCode, model);

        ContestParticipant contestParticipant = contestParticipantRepository.findOne(personId);

        model.addAttribute("contestParticipant", contestParticipant);
        return "people/personDetail";
    }
}
