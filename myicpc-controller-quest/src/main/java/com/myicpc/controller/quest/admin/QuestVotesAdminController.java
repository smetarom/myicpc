package com.myicpc.controller.quest.admin;

import com.myicpc.controller.GeneralAdminController;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.quest.QuestLeaderboard;
import com.myicpc.repository.quest.QuestLeaderboardRepository;
import com.myicpc.service.quest.QuestMngmService;
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
import java.util.Arrays;

/**
 * @author Roman Smetana
 */
@Controller
public class QuestVotesAdminController extends GeneralAdminController {

    @Autowired
    private QuestMngmService questMngmService;

    @RequestMapping(value = "/private/{contestCode}/quest/votes", method = RequestMethod.GET)
    public String submissions(@PathVariable final String contestCode, Model model) {
        Contest contest = getContest(contestCode, model);


        return "private/quest/votes";
    }
}
