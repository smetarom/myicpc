package com.myicpc.controller.social.admin.poll;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import com.myicpc.controller.GeneralAdminController;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Problem;
import com.myicpc.model.poll.Poll;
import com.myicpc.model.teamInfo.TeamInfo;
import com.myicpc.model.teamInfo.University;
import com.myicpc.repository.eventFeed.ProblemRepository;
import com.myicpc.repository.poll.PollRepository;
import com.myicpc.repository.teamInfo.TeamInfoRepository;
import com.myicpc.repository.teamInfo.UniversityRepository;
import com.myicpc.social.poll.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Roman Smetana
 */
@Controller
@SessionAttributes("poll")
public class PollAdminController extends GeneralAdminController {

    @Autowired
    private TeamInfoRepository teamInfoRepository;

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private PollService pollService;

    @Autowired
    private PollRepository pollRepository;

    @RequestMapping(value = "/private/{contestCode}/polls", method = RequestMethod.GET)
    public String polls(@PathVariable String contestCode, Model model) {
        Contest contest = getContest(contestCode, model);
        List<Poll> polls = pollRepository.findByContestOrderByStartDateDesc(contest);
        model.addAttribute("polls", polls);
        return "private/poll/polls";
    }

    @RequestMapping(value = "/private/{contestCode}/poll/create", method = RequestMethod.GET)
    public String createPoll(@PathVariable String contestCode, final Model model) {
        Contest contest = getContest(contestCode, model);

        Poll poll = new Poll();
        poll.setContest(contest);
        model.addAttribute("poll", poll);
        model.addAttribute("mode", "create");
        model.addAttribute("pollTypes", pollService.getPollTypes());
        model.addAttribute("headlineTitle", getMessage("pollAdmin.create"));
        return "private/poll/editPoll";
    }

    /**
     * Shows an edit existing poll page
     *
     * @param pollId
     *            poll ID
     * @param model
     * @return view
     */
    @RequestMapping(value = "/private/{contestCode}/poll/{pollId}/edit", method = RequestMethod.GET)
    public String editPoll(@PathVariable final String contestCode, @PathVariable final Long pollId, final Model model,
                           RedirectAttributes redirectAttributes) {
        getContest(contestCode, model);
        Poll poll = pollService.findEditPollById(pollId);

        if (poll == null) {
            errorMessage(redirectAttributes, "pollAdmin.poll.notFound");
            return "redirect:/private" + getContestURL(contestCode) + "/polls";
        }

        model.addAttribute("poll", poll);
        model.addAttribute("mode", "edit");
        model.addAttribute("pollTypes", pollService.getPollTypes());
        model.addAttribute("choiceCount", poll.getOptions().size());
        model.addAttribute("headlineTitle", getMessage("pollAdmin.edit"));
        return "private/poll/editPoll";
    }

    @RequestMapping(value = "/private/{contestCode}/poll/{pollId}/resolve", method = RequestMethod.GET)
    public String startResolvePoll(@PathVariable final String contestCode, @PathVariable final Long pollId, final Model model) {
        getContest(contestCode, model);
        Poll poll = pollService.findEditPollById(pollId);

        if (poll.getCorrectAnswer() != null) {
            model.addAttribute("warnMsg", getMessage("poll.alreadyResolved"));
        }

        model.addAttribute("poll", poll);
        return "private/poll/resolvePoll";
    }

    @RequestMapping(value = "/private/{contestCode}/poll/{pollId}/delete", method = RequestMethod.GET)
    public String deletePoll(@PathVariable final String contestCode, @PathVariable final Long pollId, final RedirectAttributes redirectAttributes) {
        pollRepository.delete(pollId);
        successMessage(redirectAttributes, "delete.success");
        return "redirect:/private" + getContestURL(contestCode) + "/polls";
    }

    @RequestMapping(value = "/private/{contestCode}/poll/update", method = RequestMethod.POST)
    public String updatePoll(@PathVariable String contestCode, @Valid @ModelAttribute("poll") Poll poll, BindingResult result, Model model) {
        Contest contest = getContest(contestCode, model);
        model.addAttribute("headlineTitle", getMessage("pollAdmin.edit"));
        if (result.hasErrors()) {
            return "private/poll/editPoll";
        } else if (!poll.getStartDate().before(poll.getEndDate())) {
            model.addAttribute("errorMsg", getMessage("poll.error.dates"));
            return "private/poll/editPoll";
        } else if (poll.getChoiceStringList() == null || poll.getChoiceStringList().isEmpty()) {
            model.addAttribute("errorMsg", getMessage("poll.error.noChoice"));
            return "private/poll/editPoll";
        }

        pollService.updatePoll(poll);
        return "redirect:/private" + getContestURL(contestCode) + "/polls";
    }

    @RequestMapping(value = "/private/{contestCode}/poll/resolve", method = RequestMethod.POST)
    public String resolvePoll(@PathVariable String contestCode, @ModelAttribute("poll") Poll poll, RedirectAttributes redirectAttributes) {
        Contest contest = getContest(contestCode, null);
        pollService.resolvePoll(poll);
        successMessage(redirectAttributes, "pollAdmin.resolve.success", poll.getQuestion());

        return "redirect:/private" + getContestURL(contestCode) + "/polls";
    }

    @RequestMapping(value = "/private/{contestCode}/poll/all-teams", method = RequestMethod.GET)
    @ResponseBody
    public String allTeams(@PathVariable String contestCode) {
        Contest contest = getContest(contestCode, null);
        JsonArray arr = new JsonArray();
        Iterable<TeamInfo> teams = teamInfoRepository.findByContestOrderByNameAsc(contest);
        for (TeamInfo teamInfo : teams) {
            if (contest.isShowTeamNames()) {
                arr.add(new JsonPrimitive(teamInfo.getName()));
            } else if (teamInfo.getUniversity() != null) {
                arr.add(new JsonPrimitive(teamInfo.getUniversity().getName()));
            }
        }
        return arr.toString();
    }

    @RequestMapping(value = "/private/{contestCode}/poll/all-universities", method = RequestMethod.GET)
    @ResponseBody
    public String allUniversities(@PathVariable String contestCode) {
        Contest contest = getContest(contestCode, null);
        JsonArray arr = new JsonArray();
        Set<University> universities = new HashSet<>();
        Iterable<TeamInfo> teams = teamInfoRepository.findByContestOrderByNameAsc(contest);
        for (TeamInfo team : teams) {
            if (team.getUniversity() != null) {
                universities.add(team.getUniversity());
            }
        }
        for (University university : universities) {
            arr.add(new JsonPrimitive(university.getName()));
        }
        return arr.toString();
    }

    @RequestMapping(value = "/private/{contestCode}/poll/all-problems", method = RequestMethod.GET)
    @ResponseBody
    public String allProblems(@PathVariable String contestCode) {
        Contest contest = getContest(contestCode, null);
        JsonArray arr = new JsonArray();
        Iterable<Problem> problems = problemRepository.findByContestOrderByCodeAsc(contest);
        for (Problem problem : problems) {
            arr.add(new JsonPrimitive(problem.getCode() + " - " + problem.getName()));
        }
        return arr.toString();
    }

    /**
     * Bind date time format
     *
     * @param binder
     */
    @InitBinder("poll")
    protected void initEventBinder(WebDataBinder binder) {
        bindDateTimeFormat(binder);
    }
}
