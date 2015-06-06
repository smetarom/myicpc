package com.myicpc.controller.scoreboard;

import com.myicpc.controller.GeneralController;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Problem;
import com.myicpc.repository.eventFeed.ProblemRepository;
import com.myicpc.service.scoreboard.problem.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author Roman Smetana
 */
@Controller
public class ProblemController extends GeneralController {
    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private ProblemService problemService;

    @RequestMapping(value = "/{contestCode}/problem/{problemCode}/detail", method = RequestMethod.GET)
    public String problemDetail(@PathVariable String contestCode, @PathVariable String problemCode, Model model, RedirectAttributes redirectAttributes) {
        Contest contest = getContest(contestCode, model);

        Problem problem = problemRepository.findByCodeAndContest(problemCode, contest);
        if (problem == null) {
            errorMessage(redirectAttributes, "problem.notFound", problemCode);
            return "redirect:/" + getContestURL(contestCode);
        }

        model.addAttribute("problem", problem);

        return "scoreboard/problem/problemDetail";
    }

    @RequestMapping(value = "/{contestCode}/problem/{problemCode}/attemps-template", method = RequestMethod.GET)
    public String attemptTemplate(@PathVariable String contestCode, @PathVariable String problemCode, Model model) {
        Contest contest = getContest(contestCode, model);
        Problem problem = problemRepository.findByCodeAndContest(problemCode, contest);

        model.addAttribute("problem", problem);
        return "scoreboard/problem/template/attempts";
    }

    @ResponseBody
    @RequestMapping(value = "/{contestCode}/problem/{problemCode}/attempts-data", method = RequestMethod.GET)
    public String problemAttemptJSON(@PathVariable String contestCode, @PathVariable String problemCode) {
        Contest contest = getContest(contestCode, null);
        Problem problem = problemRepository.findByCodeAndContest(problemCode, contest);
        return problemService.getSubmissionAttemptsJSON(problem).toString();
    }


    @RequestMapping(value = "/{contestCode}/problem/{problemCode}/teams-template", method = RequestMethod.GET)
    public String teamsTempate(@PathVariable String contestCode, @PathVariable String problemCode, Model model) {
        Contest contest = getContest(contestCode, model);
        Problem problem = problemRepository.findByCodeAndContest(problemCode, contest);

        model.addAttribute("problem", problem);
        return "scoreboard/problem/template/teams";
    }
}
