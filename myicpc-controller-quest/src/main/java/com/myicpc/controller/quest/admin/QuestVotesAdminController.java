package com.myicpc.controller.quest.admin;

import com.myicpc.controller.GeneralAdminController;
import com.myicpc.model.contest.Contest;
import com.myicpc.repository.quest.QuestSubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller for vote management
 *
 * @author Roman Smetana
 */
@Controller
public class QuestVotesAdminController extends GeneralAdminController {

    @Autowired
    private QuestSubmissionRepository submissionRepository;

    @RequestMapping(value = "/private/{contestCode}/quest/votes", method = RequestMethod.GET)
    public String submissions(@PathVariable final String contestCode, Model model) {
        Contest contest = getContest(contestCode, model);

        model.addAttribute("inProgressVoteSubmissions", submissionRepository.getVoteInProgressSubmissions(contest));
        model.addAttribute("winnerVoteSubmissions", submissionRepository.getVoteWinnersSubmissions(contest));

        return "private/quest/votes";
    }
}
