package com.myicpc.service.scoreboard.insight;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Problem;
import com.myicpc.model.eventFeed.Team;
import com.myicpc.model.eventFeed.TeamProblem;
import com.myicpc.repository.eventFeed.ProblemRepository;
import com.myicpc.repository.eventFeed.TeamProblemRepository;
import com.myicpc.service.scoreboard.dto.insight.JudgmentDTO;
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
        List<Problem> problems = problemRepository.findByContestOrderByCodeAsc(contest);
        List<ReportByProblem> reports = new ArrayList<>(problems.size());
        for (Problem problem : problems) {
            reports.add(calcReportByProblem(problem, team));
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
        ReportByProblem report = calcReportByProblem(problem, null);

        double averageSolutionTime = 0;
        double numSolvedCounter = 0;
        TeamProblem firstSubmission = null;
        TeamProblem firstSolution = null;
        Map<Team, Boolean> teamMap = new HashMap<>();
        Map<String, Integer> solvedLanguageMap = new HashMap<>();
        Map<String, Integer> languageMap = new HashMap<>();

        List<TeamProblem> teamProblems = teamProblemRepository.findByProblem(problem);
        // for each submission, it modifies statistics we have so far to get
        // final report
        for (TeamProblem teamProblem : teamProblems) {
            if (teamMap.containsKey(teamProblem.getTeam())) {
                if (teamProblem.getSolved()) {
                    teamMap.put(teamProblem.getTeam(), teamProblem.getSolved());
                }
            } else {
                teamMap.put(teamProblem.getTeam(), teamProblem.getSolved());
            }

            if (teamProblem.getSolved()) {
                if (solvedLanguageMap.containsKey(teamProblem.getLanguage())) {
                    solvedLanguageMap.put(teamProblem.getLanguage(), Integer.valueOf(solvedLanguageMap.get(teamProblem.getLanguage()) + 1));
                } else {
                    solvedLanguageMap.put(teamProblem.getLanguage(), 1);
                }
            }
            // TODO can I do better insert to map using some library?
            if (languageMap.containsKey(teamProblem.getLanguage())) {
                languageMap.put(teamProblem.getLanguage(), Integer.valueOf(languageMap.get(teamProblem.getLanguage()) + 1));
            } else {
                languageMap.put(teamProblem.getLanguage(), 1);
            }

            if (teamProblem.getSolved()) {
                averageSolutionTime += teamProblem.getTime();
                numSolvedCounter++;
            }

            if (firstSubmission == null) {
                firstSubmission = teamProblem;
            }
            if (firstSolution == null && teamProblem.isFirstSolved()) {
                firstSolution = teamProblem;
            }
        }

        int numSolvedTeams = 0;
        int numSubmittedTeams = 0;
        for (Map.Entry<Team, Boolean> entry : teamMap.entrySet()) {
            if (entry.getValue()) {
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
        report.setTotalSubmissions(teamProblems.size());
        report.setAverageSolutionTime(averageSolutionTime / numSolvedCounter);

        return report.getFullReport();
    }

    /**
     * Returns a detailed report about a problem
     *
     * @param problem
     *            problem
     * @return detailed report about a problem
     */
    private ReportByProblem calcReportByProblem(final Problem problem, final Team team) {
        List<ImmutablePair<String, Long>> results;
        if (team != null) {
            results = problemRepository.getProblemReportByTeam(problem, team);
        } else {
            results = problemRepository.getProblemReport(problem);
        }
        ReportByProblem report = new ReportByProblem(problem);
        for (ImmutablePair<String, Long> result : results) {
            report.addResult(new JudgmentDTO(result.getKey(), null, result.getValue().intValue()));
        }
        return report;
    }
}
