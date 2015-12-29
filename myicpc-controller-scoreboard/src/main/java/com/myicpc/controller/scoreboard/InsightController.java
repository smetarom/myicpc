package com.myicpc.controller.scoreboard;

import com.google.gson.JsonObject;
import com.myicpc.commons.utils.MessageUtils;
import com.myicpc.controller.GeneralController;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Language;
import com.myicpc.model.eventFeed.Problem;
import com.myicpc.model.eventFeed.Team;
import com.myicpc.repository.eventFeed.JudgementRepository;
import com.myicpc.repository.eventFeed.LanguageRepository;
import com.myicpc.repository.eventFeed.ProblemRepository;
import com.myicpc.repository.eventFeed.TeamProblemRepository;
import com.myicpc.repository.eventFeed.TeamRepository;
import com.myicpc.service.scoreboard.ScoreboardService;
import com.myicpc.service.scoreboard.insight.CodeInsightService;
import com.myicpc.service.scoreboard.insight.DashboardInsightService;
import com.myicpc.service.scoreboard.insight.LanguageInsightService;
import com.myicpc.service.scoreboard.insight.ProblemInsightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for insight pages
 *
 * @author Roman Smetana
 */
@Controller
public class InsightController extends GeneralController {

    @Autowired
    private ScoreboardService scoreboardService;

    @Autowired
    private CodeInsightService codeInsightService;

    @Autowired
    private ProblemInsightService problemInsightService;

    @Autowired
    private LanguageInsightService languageInsightService;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private JudgementRepository judgementRepository;

    @Autowired
    private TeamProblemRepository teamProblemRepository;

    @Autowired
    private DashboardInsightService dashboardInsightService;

    @RequestMapping(value = "/{contestCode}/insight", method = RequestMethod.GET)
    public String insight(@PathVariable String contestCode, Model model) {
        Contest contest = getContest(contestCode, model);

        model.addAttribute("problems", problemRepository.findByContestOrderByCodeAsc(contest));
        model.addAttribute("languages", languageRepository.findByContestOrderByName(contest));
        model.addAttribute("submissionCount", teamProblemRepository.countByTeamContest(contest));
        return "scoreboard/insight/insight";
    }

    @RequestMapping(value = "/{contestCode}/insight/template/all-problems", method = RequestMethod.GET)
    public String insightAllProblems(@PathVariable String contestCode, Model model) {
        Contest contest = getContest(contestCode, model);

        addJudgmentsToModel(model, contest);
        return "scoreboard/insight/template/allProblems";
    }

    @RequestMapping(value = "/{contestCode}/insight/template/problem-detail", method = RequestMethod.GET)
    public String insightProblemDetail(@PathVariable String contestCode, Model model) {
        Contest contest = getContest(contestCode, model);

        addJudgmentsToModel(model, contest);
        return "scoreboard/insight/template/problemDetail";
    }

    @RequestMapping(value = "/{contestCode}/insight/template/all-languages", method = RequestMethod.GET)
    public String insightAllLanguages(@PathVariable String contestCode, Model model) {
        Contest contest = getContest(contestCode, model);

        addJudgmentsToModel(model, contest);
        return "scoreboard/insight/template/allLanguages";
    }

    @RequestMapping(value = "/{contestCode}/insight/template/language-detail", method = RequestMethod.GET)
    public String insightLanguageDetail(@PathVariable String contestCode, Model model) {
        Contest contest = getContest(contestCode, model);

        addJudgmentsToModel(model, contest);
        return "scoreboard/insight/template/languageDetail";
    }

    @RequestMapping(value = "/{contestCode}/insight/template/code-insight", method = RequestMethod.GET)
    public String insightCodeInsight(@PathVariable String contestCode, Model model) {
        Contest contest = getContest(contestCode, model);

        return "scoreboard/insight/template/codeInsight";
    }

    @ResponseBody
    @RequestMapping(value = "/{contestCode}/insight/ajax/all-problems", method = RequestMethod.GET)
    public String insightAllProblemsJSON(@PathVariable String contestCode) {
        Contest contest = getContest(contestCode, null);

        JsonObject response = new JsonObject();
        response.add("data", problemInsightService.reportAll(contest));
        response.add("problems", scoreboardService.getProblemsJSON(contest));
        response.addProperty("title", MessageUtils.getMessage("insight.problems"));

        return response.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/{contestCode}/insight/ajax/team-problems/{teamId}", method = RequestMethod.GET)
    public String insightAllTeamProblemsJSON(@PathVariable String contestCode, @PathVariable Long teamId) {
        Contest contest = getContest(contestCode, null);
        Team team = teamRepository.findByExternalId(teamId);

        JsonObject response = new JsonObject();
        response.add("data", problemInsightService.reportAll(team, contest));
        response.add("problems", scoreboardService.getProblemsJSON(contest));
        response.addProperty("title", MessageUtils.getMessage("insight.problems"));

        return response.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/{contestCode}/insight/ajax/problem/{problemCode}", method = RequestMethod.GET)
    public String insightProblemDetailJSON(@PathVariable String contestCode, @PathVariable String problemCode) {
        Contest contest = getContest(contestCode, null);
        Problem problem = problemRepository.findByCodeAndContest(problemCode, contest);

        JsonObject response = new JsonObject();
        response.add("data", problemInsightService.reportSingle(problem, contest));
        response.addProperty("title", MessageUtils.getMessage("insight.problem", problem.getCode(), problem.getName()));

        return response.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/{contestCode}/insight/ajax/all-languages", method = RequestMethod.GET)
    public String insightAllLanguagesJSON(@PathVariable String contestCode) {
        Contest contest = getContest(contestCode, null);

        JsonObject response = new JsonObject();
        response.add("data", languageInsightService.reportAll(contest));
        response.addProperty("title", MessageUtils.getMessage("insight.languages"));

        return response.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/{contestCode}/insight/ajax/language/{languageName}", method = RequestMethod.GET)
    public String insightLanguageDetailJSON(@PathVariable String contestCode, @PathVariable String languageName) {
        Contest contest = getContest(contestCode, null);
        Language language = languageRepository.findByNameAndContest(languageName, contest);

        JsonObject response = new JsonObject();
        response.add("data", languageInsightService.reportSingle(language, contest));
        response.addProperty("title", MessageUtils.getMessage("insight.language", language.getName()));

        return response.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/{contestCode}/insight/ajax/codeInsight", method = RequestMethod.GET)
    public String insightCodeJSON(@PathVariable String contestCode) {
        Contest contest = getContest(contestCode, null);

        JsonObject insightReport = codeInsightService.createCodeInsightReport(contest, CodeInsightService.InsideCodeMode.DIFF);

        JsonObject response = new JsonObject();
        response.add("data", insightReport);
        response.add("problems", scoreboardService.getProblemsJSON(contest));
        response.addProperty("title", MessageUtils.getMessage("insight.problems"));

        return response.toString();
    }

    private void addJudgmentsToModel(final Model model, final Contest contest) {
        model.addAttribute("judgements", judgementRepository.findByContestOrderByNameAsc(contest));
    }

    @RequestMapping(value = "/{contestCode}/insight/overview", method = RequestMethod.GET)
    public String dashboard(@PathVariable String contestCode, Model model) {
        Contest contest = getContest(contestCode, model);

        model.addAttribute("data", dashboardInsightService.generateStatistics(contest));
        return "scoreboard/insight/insightOverview";
    }

    @RequestMapping(value = "/{contestCode}/insight/ajax/overview", method = RequestMethod.GET)
    public String insightCodeJSON(@PathVariable String contestCode, Model model) {
        Contest contest = getContest(contestCode, null);

        model.addAttribute("data", dashboardInsightService.generateStatistics(contest));
        return "scoreboard/insight/fragment/insightOverview";
    }
}
