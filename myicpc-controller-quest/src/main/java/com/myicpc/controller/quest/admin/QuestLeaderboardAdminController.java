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

/**
 * Controller for {@link QuestLeaderboard} management
 * <p/>
 * It takes care about CRUD operations
 *
 * @author Roman Smetana
 */
@Controller
@SessionAttributes("leaderboard")
public class QuestLeaderboardAdminController extends GeneralAdminController {

    @Autowired
    private QuestLeaderboardRepository leaderboardRepository;

    @Autowired
    private QuestMngmService questMngmService;

    @RequestMapping(value = "/private/{contestCode}/quest/leaderboards", method = RequestMethod.GET)
    public String submissions(@PathVariable final String contestCode, Model model) {
        Contest contest = getContest(contestCode, model);

        model.addAttribute("leaderboards", leaderboardRepository.findByContestOrderByNameAsc(contest));

        return "private/quest/leaderboards";
    }

    @RequestMapping(value = "/private/{contestCode}/quest/leaderboard/create", method = RequestMethod.GET)
    public String createLeaderboard(@PathVariable final String contestCode, final Model model) {
        Contest contest = getContest(contestCode, model);
        QuestLeaderboard leaderboard = new QuestLeaderboard();
        leaderboard.setContest(contest);

        model.addAttribute("leaderboard", leaderboard);
        model.addAttribute("roles", questMngmService.getAllContestParticipantRoles());
        model.addAttribute("headlineTitle", getMessage("questAdmin.leaderboards.create"));

        return "private/quest/editLeaderboard";
    }

    @RequestMapping(value = "/private/{contestCode}/quest/leaderboard/{leaderboardId}/edit", method = RequestMethod.GET)
    public String editLeaderboard(@PathVariable final String contestCode, @PathVariable final Long leaderboardId, final Model model,
                                  final RedirectAttributes redirectAttributes) {
        Contest contest = getContest(contestCode, model);
        QuestLeaderboard leaderboard = leaderboardRepository.findOne(leaderboardId);

        if (leaderboard == null) {
            errorMessage(redirectAttributes, "questAdmin.leaderboards.notFound");
            return "redirect:/private" + getContestURL(contestCode) + "/quest/leaderboards";
        }

        model.addAttribute("leaderboard", leaderboard);
        model.addAttribute("roles", questMngmService.getAllContestParticipantRoles());
        model.addAttribute("headlineTitle", getMessage("questAdmin.leaderboards.edit"));

        return "private/quest/editLeaderboard";
    }

    @RequestMapping(value = "/private/{contestCode}/quest/leaderboard/{leaderboardId}/delete", method = RequestMethod.GET)
    public String deleteLeaderboard(@PathVariable final String contestCode, @PathVariable final Long leaderboardId, final Model model,
                                    final RedirectAttributes redirectAttributes) {
        leaderboardRepository.delete(leaderboardId);
        successMessage(redirectAttributes, "questAdmin.leaderboards.deleted");
        return "redirect:/private" + getContestURL(contestCode) + "/quest/leaderboards";
    }

    @RequestMapping(value = "/private/{contestCode}/quest/leaderboard/update", method = RequestMethod.POST)
    public String updateLeaderboard(@Valid @ModelAttribute("leaderboard") final QuestLeaderboard leaderboard, @PathVariable final String contestCode,
                                    @RequestParam String[] roles,
                                    final BindingResult result, final Model model, final RedirectAttributes redirectAttributes) {
        Contest contest = getContest(contestCode, model);
        model.addAttribute("headlineTitle", getMessage("questAdmin.leaderboards.edit"));
        if (result.hasErrors()) {
            return "private/quest/editLeaderboard";
        }
        leaderboard.setParsedRoles(roles);

        leaderboardRepository.save(leaderboard);
        successMessage(redirectAttributes, "save.success");

        return "redirect:/private" + getContestURL(contestCode) + "/quest/leaderboards";
    }
}
