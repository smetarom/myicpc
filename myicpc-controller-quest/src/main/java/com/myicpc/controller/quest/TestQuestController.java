package com.myicpc.controller.quest;

import com.myicpc.controller.GeneralController;
import com.myicpc.model.contest.Contest;
import com.myicpc.service.quest.QuestSubmissionService;
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
public class TestQuestController extends GeneralController {

    @Autowired
    private QuestSubmissionService questSubmissionService;

    @RequestMapping(value = "/{contestCode}/submissions/parse", method = RequestMethod.GET)
    public void questHomepage(@PathVariable final String contestCode, Model model) {
        Contest contest = getContest(contestCode, model);

        questSubmissionService.processAllSubmissions(contest);
//        questSubmissionService.processRecentSubmissions(contest);
    }
}
