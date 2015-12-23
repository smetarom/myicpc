package com.myicpc.controller.scoreboard.admin;

import com.myicpc.controller.GeneralAdminController;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.JudgementColor;
import com.myicpc.model.poll.Poll;
import com.myicpc.model.teamInfo.TeamInfo;
import com.myicpc.repository.eventFeed.JudgementColorRepository;
import com.myicpc.repository.teamInfo.ContestParticipantRepository;
import com.myicpc.repository.teamInfo.TeamInfoRepository;
import com.myicpc.service.exception.WebServiceException;
import com.myicpc.service.scoreboard.problem.ProblemService;
import com.myicpc.service.scoreboard.team.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.ValidationException;
import java.io.IOException;
import java.util.List;

/**
 * Controller for contest judgment management
 *
 * @author Roman Smetana
 */
@Controller
public class JudgmentAdminController extends GeneralAdminController {
    @Autowired
    private JudgementColorRepository judgementColorRepository;

    @Autowired
    private ProblemService problemService;

    @RequestMapping(value = "/private/{contestCode}/judgments", method = RequestMethod.GET)
    public String judgementColors(@PathVariable String contestCode, Model model) {
        Contest contest = getContest(contestCode, model);

        List<JudgementColor> judgementColors = judgementColorRepository.findByContest(contest);

        model.addAttribute("newJudgementColor", new JudgementColor());
        model.addAttribute("defaultColors", JudgementColor.getDefaultColors());
        model.addAttribute("judgementColors", judgementColors);
        return "private/judgment/judgments";
    }

    @RequestMapping(value = "/private/{contestCode}/judgments", method = RequestMethod.POST)
    public String createJudgementColor(@ModelAttribute JudgementColor newJudgementColor,
                                       @PathVariable String contestCode, Model model,
                                       RedirectAttributes redirectAttributes) {
        Contest contest = getContest(contestCode, model);

        newJudgementColor.setContest(contest);
        problemService.createJudgmentColor(newJudgementColor);
        model.addAttribute("headlineTitle", getMessage("pollAdmin.edit"));

        return "redirect:/private/" + getContestURL(contestCode) + "/judgments";
    }

    @RequestMapping(value = "/private/{contestCode}/judgment/{judgmentColorId}/delete", method = RequestMethod.GET)
    public String deleteJudgementColors(@PathVariable String contestCode, @PathVariable Long judgmentColorId,
                                        Model model, RedirectAttributes redirectAttributes) {
        Contest contest = getContest(contestCode, model);

        JudgementColor judgementColor = judgementColorRepository.findOne(judgmentColorId);
        if (judgementColor != null) {
            problemService.deleteJudgmentColor(judgementColor);
        }
        successMessage(redirectAttributes, "delete.success");

        return "redirect:/private/" + getContestURL(contestCode) + "/judgments";
    }
}
