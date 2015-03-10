package com.myicpc.controller.quest.admin;

import com.myicpc.controller.GeneralAdminController;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.quest.QuestChallenge;
import com.myicpc.model.quest.QuestParticipant;
import com.myicpc.repository.quest.QuestChallengeRepository;
import com.myicpc.repository.quest.QuestParticipantRepository;
import com.myicpc.service.exception.BusinessValidationException;
import com.myicpc.service.quest.QuestMngmService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Roman Smetana
 */
@Controller
public class QuestParticipantAdminController extends GeneralAdminController {
    private static final Logger logger = LoggerFactory.getLogger(QuestParticipantAdminController.class);

    @Autowired
    private QuestMngmService questMngmService;

    @Autowired
    private QuestParticipantRepository participantRepository;

    @RequestMapping(value = "/private/{contestCode}/quest/participants", method = RequestMethod.GET)
    public String questParticipants(@PathVariable final String contestCode, final Model model) {
        Contest contest = getContest(contestCode, model);
        List<QuestParticipant> participants = participantRepository.findByContestOrderByContestParticipantLastnameAsc(contest);

        model.addAttribute("participants", participants);

        return "private/quest/questParticipants";
    }

    @RequestMapping(value = "/private/{contestCode}/quest/participants", method = RequestMethod.POST)
    public String updateParticipants(@PathVariable final String contestCode, final HttpServletRequest request,
                                     final RedirectAttributes redirectAttributes) {
        Contest contest = getContest(contestCode, null);

        questMngmService.bulkParticipantUpdate(request.getParameterMap(), contest);
        successMessage(redirectAttributes, "save.success");

        return "redirect:/private" + getContestURL(contestCode) + "/quest/participants";
    }
}
