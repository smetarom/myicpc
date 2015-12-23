package com.myicpc.service.scoreboard.insight;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myicpc.dto.insight.InsightSubmissionDTO;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Problem;
import com.myicpc.model.eventFeed.Team;
import com.myicpc.repository.eventFeed.ProblemRepository;
import com.myicpc.dto.insight.JudgmentDTO;
import com.myicpc.service.scoreboard.dto.insight.ReportByProblem;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service that provides business logic for generating insight report
 * with focus on submission by problems
 *
 * @author Roman Smetana
 */
@Service
@Transactional
public class ProblemInsightService extends AbstractInsightService<Problem> {
    @Autowired
    private ProblemRepository problemRepository;

    /**
     * Returns a report for all problems. For each problem, it calculates
     * judgments
     *
     * <pre>
     * [
     *   {
     *     "code": "&lt;PROBLEM_LETTER_CODE&gt;",
     *     "data": [
     *       {
     *         "key": "&lt;JUDGMENT_CODE&gt;",
     *         "value": &lt;NUMBER_OF_SUBMISSIONS&gt;,
     *         "color": "&lt;JUDGMENT_COLOR&gt;"
     *       },
     *       ...
     *     ]
     *   },
     * ...
     * ]
     * </pre>
     *
     * @return JSON report
     */
    @Override
    public JsonArray reportAll(final Contest contest) {
        return reportAll(null, contest);
    }

    public JsonArray reportAll(final Team team, final Contest contest) {
        Map<String, String> judgmentColors = getJudgmentColors(contest);
        List<Problem> problems = problemRepository.findByContestOrderByCodeAsc(contest);
        List<ReportByProblem> reports = new ArrayList<>(problems.size());
        for (Problem problem : problems) {
            reports.add(calcReportByProblem(problem, team, judgmentColors));
        }

        JsonArray arr = new JsonArray();
        for (ReportByProblem reportByProblem : reports) {
            arr.add(reportByProblem.getSimpleReport());
        }
        return arr;
    }

    /**
     * Returns a detailed report about a problem
     *
     * <pre>
     * {
     *   "code": "&lt;PROBLEM_LETTER_CODE&gt;",
     *   "data": [
     *     {
     *       "key": "&lt;JUDGMENT_CODE&gt;",
     *       "value": &lt;NUMBER_OF_SUBMISSIONS&gt;,
     *       "color": "&lt;JUDGMENT_COLOR&gt";
     *     },
     *     ...
     *   ],
     *   "numSolvedTeams": &lt;NUMBER_OF_TEAMS_THAT_SOLVED_THIS_PROBLEM&gt;,
     *   "numSubmittedTeams": &lt;NUMBER_OF_TEAMS_THAT_SUBMITTED_THIS_PROBLEM&gt;,
     *   "totalSubmissions": &lt;TOTAL_NUMBER_OF_SUBMISSIONS&gt;,
     *   "averageSolutionTime": "&lt;AVERAGE_SOLUTION_TIME&gt;",
     *   "languages": [
     *     {
     *       "name": "&lt;LANGUAGE_NAME&gt;",
     *       "count": &lt;NUMBER_OF_SUBMISSIONS&gt;
     *     },
     *     ...
     *   ],
     *   "solvedLanguages": [
     *     {
     *       "name": "&lt;LANGUAGE_NAME&gt;",
     *       "count": &lt;NUMBER_OF_SUBMISSIONS&gt;
     *     },
     *     ...
     *   ],
     *   "firstSubmission": {
     *     "teamId": &lt;TEAM_ID&gt;,
     *     "teamName": "&lt;TEAM_NAME&gt;",
     *     "time": "&lt;TIME&gt;"
     *   },
     *   "firstSolution": {
     *     "teamId": &lt;TEAM_ID&gt;,
     *     "teamName": "&lt;TEAM_NAME&gt;",
     *     "time": "&lt;TIME&gt;"
     *   }
     * }
     * </pre>
     *
     * @param problem
     * @return JSON report
     */
    @Override
    public JsonObject reportSingle(final Problem problem, final Contest contest) {
        Map<String, String> judgmentColors = getJudgmentColors(contest);
        ReportByProblem report = calcReportByProblem(problem, null, judgmentColors);

        double averageSolutionTime = 0;
        double numSolvedCounter = 0;
        InsightSubmissionDTO firstSubmission = null;
        InsightSubmissionDTO firstSolution = null;
        Map<Long, Boolean> teamMap = new HashMap<>();
        Map<String, Integer> solvedLanguageMap = new HashMap<>();
        Map<String, Integer> languageMap = new HashMap<>();

        List<InsightSubmissionDTO> teamSubmissions = teamProblemRepository.findByProblemInsight(problem);
        // for each submission, it modifies statistics we have so far to get
        // final report
        for (InsightSubmissionDTO teamSubmission : teamSubmissions) {
            if (teamMap.containsKey(teamSubmission.getTeamId())) {
                if (teamSubmission.isSolved()) {
                    teamMap.put(teamSubmission.getTeamId(), teamSubmission.isSolved());
                }
            } else {
                teamMap.put(teamSubmission.getTeamId(), teamSubmission.isSolved());
            }

            if (teamSubmission.isSolved()) {
                if (solvedLanguageMap.containsKey(teamSubmission.getLanguage())) {
                    solvedLanguageMap.put(teamSubmission.getLanguage(), solvedLanguageMap.get(teamSubmission.getLanguage()) + 1);
                } else {
                    solvedLanguageMap.put(teamSubmission.getLanguage(), 1);
                }
            }
            if (languageMap.containsKey(teamSubmission.getLanguage())) {
                languageMap.put(teamSubmission.getLanguage(), languageMap.get(teamSubmission.getLanguage()) + 1);
            } else {
                languageMap.put(teamSubmission.getLanguage(), 1);
            }

            if (teamSubmission.isSolved()) {
                averageSolutionTime += teamSubmission.getTime();
                numSolvedCounter++;
            }

            if (firstSubmission == null) {
                firstSubmission = teamSubmission;
            }
            if (firstSolution == null && teamSubmission.isFirstSolved()) {
                firstSolution = teamSubmission;
            }
        }

        int numSolvedTeams = 0;
        int numSubmittedTeams = 0;
        for (Boolean solved : teamMap.values()) {
            if (solved) {
                numSolvedTeams++;
            } else {
                numSubmittedTeams++;
            }
        }

        report.setFirstSolution(firstSolution);
        report.setFirstSubmission(firstSubmission);
        report.setLanguageMap(languageMap);
        report.setSolvedLanguageMap(solvedLanguageMap);
        report.setNumSolvedTeams(numSolvedTeams);
        report.setNumSubmittedTeams(numSubmittedTeams);
        report.setTotalSubmissions(teamSubmissions.size());
        report.setAverageSolutionTime(averageSolutionTime / numSolvedCounter);

        return report.getFullReport();
    }

    /**
     * Returns a detailed report about a problem
     *
     * @param problem
     *            problem
     * @param judgmentColors judgement colors
     * @return detailed report about a problem
     */
    private ReportByProblem calcReportByProblem(final Problem problem, final Team team, Map<String, String> judgmentColors) {
        List<ImmutablePair<String, Long>> results;
        if (team != null) {
            results = problemRepository.getProblemReportByTeam(problem, team);
        } else {
            results = problemRepository.getProblemReport(problem);
        }
        ReportByProblem report = new ReportByProblem(problem, judgmentColors);
        for (ImmutablePair<String, Long> result : results) {
            report.addResult(new JudgmentDTO(result.getKey(), null, result.getValue().intValue()));
        }
        return report;
    }
}
