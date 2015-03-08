package com.myicpc.controller.quest;

import com.myicpc.controller.GeneralAdminController;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.quest.QuestChallenge;
import com.myicpc.repository.quest.QuestChallengeRepository;
import com.myicpc.service.exception.BusinessValidationException;
import com.myicpc.service.quest.QuestMngmService;
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

import javax.validation.Valid;

/**
 * @author Roman Smetana
 */
@Controller
@SessionAttributes({ "challenge", "submissionFilter" })
public class QuestAdminController extends GeneralAdminController {

    @Autowired
    private QuestMngmService questMngmService;

    @Autowired
    private QuestChallengeRepository challengeRepository;

    @RequestMapping(value = "/private/{contestCode}/quest/challenges", method = RequestMethod.GET)
    public String challegeList(@PathVariable final String contestCode, Model model) {
        Contest contest = getContest(contestCode, model);
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "startDate"), new Sort.Order("name"));
        model.addAttribute("challenges", challengeRepository.findByContest(contest, sort));

        return "private/quest/challenges";
    }

    @RequestMapping(value = "/private/{contestCode}/quest/challenge/create", method = RequestMethod.GET)
    public String createChallenge(@PathVariable final String contestCode, final Model model, final RedirectAttributes redirectAttributes) {
        Contest contest = getContest(contestCode, model);
        QuestChallenge challenge = new QuestChallenge();
        challenge.setContest(contest);

        model.addAttribute("challenge", challenge);
        model.addAttribute("questHashtagPrefix", contest.getQuestConfiguration().getHashtagPrefix());
        model.addAttribute("mode", "create");
        model.addAttribute("headlineTitle", getMessage("questAdmin.create"));

        return "private/quest/editChallenge";
    }

    @RequestMapping(value = "/private/{contestCode}/quest/challenge/{challengeId}/edit", method = RequestMethod.GET)
    public String editChallenge(@PathVariable final String contestCode, @PathVariable final Long challengeId, final Model model,
                                final RedirectAttributes redirectAttributes) {
        Contest contest = getContest(contestCode, model);
        QuestChallenge challenge = challengeRepository.findOne(challengeId);

        if (challenge == null) {
            errorMessage(redirectAttributes, "questAdmin.challenge.notFound");
            return "redirect:/private" + getContestURL(contestCode) + "/quest/challenges";
        }

        model.addAttribute("challenge", challenge);
        model.addAttribute("questHashtagPrefix", contest.getQuestConfiguration().getHashtagPrefix());
        model.addAttribute("headlineTitle", getMessage("questAdmin.edit"));
        model.addAttribute("mode", "edit");

        return "private/quest/editChallenge";
    }

    @RequestMapping(value = "/private/{contestCode}/quest/challenge/{challengeId}/delete", method = RequestMethod.GET)
    public String deleteChallenge(@PathVariable final String contestCode, @PathVariable final Long challengeId, final Model model,
                                  final RedirectAttributes redirectAttributes) {
        QuestChallenge challenge = challengeRepository.findOne(challengeId);

        if (challenge == null) {
            errorMessage(redirectAttributes, "questAdmin.challenge.notFound");
            return "redirect:/private" + getContestURL(contestCode) + "/quest/challenges";
        }

        questMngmService.deleteQuestChallenge(challenge);
        successMessage(redirectAttributes, "questAdmin.challenge.deleted", challenge.getName());
        return "redirect:/private" + getContestURL(contestCode) + "/quest/challenges";
    }

    @RequestMapping(value = "/private/{contestCode}/quest/challenge/update", method = RequestMethod.POST)
    public String updateChallenge(@Valid @ModelAttribute("challenge") final QuestChallenge challenge, @PathVariable final String contestCode,
                                  final BindingResult result, final Model model, final RedirectAttributes redirectAttributes) {
        Contest contest = getContest(contestCode, model);
        model.addAttribute("questHashtagPrefix", contest.getQuestConfiguration().getHashtagPrefix());
        model.addAttribute("headlineTitle", getMessage("questAdmin.edit"));
        if (result.hasErrors()) {
            return "private/quest/editChallenge";
        }

        try {
            questMngmService.updateQuestChallenge(challenge);
            successMessage(redirectAttributes, "save.success");
        } catch (BusinessValidationException ex) {
            result.rejectValue("hashtagSuffix", ex.getMessageCode());
            return "private/quest/editChallenge";
        }
        return "redirect:/private" + getContestURL(contestCode) + "/quest/challenges";
    }

    @InitBinder("challenge")
    protected void initEventBinder(final WebDataBinder binder) {
        bindDateTimeFormatAllowEmpty(binder);
    }
}