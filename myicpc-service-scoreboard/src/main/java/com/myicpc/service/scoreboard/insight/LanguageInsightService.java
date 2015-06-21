package com.myicpc.service.scoreboard.insight;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myicpc.dto.insight.InsightSubmissionDTO;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Judgement;
import com.myicpc.model.eventFeed.Language;
import com.myicpc.model.eventFeed.Team;
import com.myicpc.model.eventFeed.TeamProblem;
import com.myicpc.repository.eventFeed.JudgementRepository;
import com.myicpc.repository.eventFeed.LanguageRepository;
import com.myicpc.service.scoreboard.dto.insight.JudgmentDTO;
import com.myicpc.service.scoreboard.dto.insight.LanguageDTO;
import com.myicpc.service.scoreboard.dto.insight.ReportByJudgement;
import com.myicpc.service.scoreboard.dto.insight.ReportByLanguage;
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
 * with focus on submission language
 *
 * @author Roman Smetana
 */
@Service
@Transactional
public class LanguageInsightService extends AbstractInsightService<Language> {
    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private JudgementRepository judgementRepository;

    /**
     * Returns a report for all languages. For each language, it calculates
     * judgments
     *
     * <pre>
     * [
     *   {
     *     "key": "&lt;JUDGMENT_CODE&gt;",
     *     "description": "&lt;JUDGMENT_DESCRIPTION&gt;",
     *     "color": "&lt;JUDGMENT_COLOR&gt;",
     *     "values": [
     *       [
     *         "&lt;LANGUAGE_NAME&gt;",
     *         &lt;NUMBER_OF_SUBMISSIONS&gt;
     *       ],
     *       ...
     *     ]
     *   },
     *   ...
     * ]
     * </pre>
     *
     * @param contest
     * @return JSON report
     */
    @Override
    public JsonArray reportAll(Contest contest) {
        Iterable<Judgement> judgements = judgementRepository.findByContest(contest);
        List<ReportByJudgement> reports = new ArrayList<>();

        Iterable<Language> languages = languageRepository.findAll();
        Map<String, Boolean> languageMap = new HashMap<>();
        for (Language language : languages) {
            languageMap.put(language.getName(), false);
        }

        for (Judgement judgement : judgements) {
            List<ImmutablePair<String, Long>> results = judgementRepository.getJudgmentReport(judgement.getCode());
            ReportByJudgement report = new ReportByJudgement(judgement);
            for (ImmutablePair<String, Long> result : results) {
                report.addLanguage(new LanguageDTO(result.getKey(), result.getValue().intValue()));
                languageMap.put(result.getKey(), true);
            }
            for (Map.Entry<String, Boolean> entry : languageMap.entrySet()) {
                if (!entry.getValue()) {
                    report.addLanguage(new LanguageDTO(entry.getKey(), 0));
                }
                languageMap.put(entry.getKey(), false);
            }

            reports.add(report);
        }

        JsonArray arr = new JsonArray();
        // creates a JSON representation
        for (ReportByJudgement reportByJudgement : reports) {
            JsonObject o = reportByJudgement.getFullReport();
            if (o != null) {
                arr.add(o);
            }
        }
        return arr;
    }

    /**
     * Returns a detailed report about a language
     *
     * <pre>
     * {
     *   "name": "&lt;LANGUAGE_NAME&gt;",
     *   "data": [
     *     {
     *       "key": "&lt;JUDGMENT_CODE&gt;",
     *       "name": "&lt;JUDGMENT_DESCRIPTION&gt;",
     *       "value": &lt;NUMBER_OF_SUBMISSIONS&gt;,
     *       "color": "&lt;JUDGMENT_COLOR&gt;"
     *     },
     *     ...
     *   ],
     *   "numProblemSolved": &lt;NUMBER_OF_TEAMS_SOLVED_PROBLEM_WITH_LANGUAGE&gt;,
     *   "numProblemSubmitted": &lt;NUMBER_OF_TEAMS_SUBMITTED_NOT_SOLVED_PROBLEM_WITH_LANGUAGE&gt;,
     *   "totalSubmissions": &lt;TOTAL_NUMBER_OF_SUBMISSIONS&gt;,
     *   "usagePercentage": &lt;PERCENTAGE_USAGE_FROM_ALL_SUBMISSIONS&gt;,
     *   "usedByNumTeams": &lt;TOTAL_NUMBER_OF_TEAMS_USING_LANGUAGE&gt;,
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
     * @param language
     * @param contest
     * @return JSON report
     */
    @Override
    public JsonObject reportSingle(Language language, Contest contest) {
        ReportByLanguage report = new ReportByLanguage(language.getName());
        int numProblemSolved = 0;
        int numProblemSubmitted = 0;
        int usedByNumTeams = 0;
        InsightSubmissionDTO firstSolution = null;
        InsightSubmissionDTO firstSubmission = null;
        Map<Long, Boolean> teamMap = new HashMap<>();
        // load all judgments and number of submissions for judgment if there is
        // at least one submission per judgment
        List<ImmutablePair<String, Long>> results = languageRepository.getLanguageReport(language.getName());
        for (ImmutablePair<String, Long> result : results) {
            report.addResult(new JudgmentDTO(result.getKey(), null, result.getValue().intValue()));
        }

        List<InsightSubmissionDTO> teamProblems = teamProblemRepository.findByLanguageAndContest(language.getName(), contest);
        long totalSubmissions = teamProblemRepository.countByTeamContest(contest);

        // get the first submitted and solved submissions
        for (InsightSubmissionDTO teamSubmissionDTO : teamProblems) {
            if (teamMap.containsKey(teamSubmissionDTO.getTeamId())) {
                if (teamSubmissionDTO.isSolved()) {
                    teamMap.put(teamSubmissionDTO.getTeamId(), teamSubmissionDTO.isSolved());
                }
            } else {
                teamMap.put(teamSubmissionDTO.getTeamId(), teamSubmissionDTO.isSolved());
            }

            if (firstSubmission == null) {
                firstSubmission = teamSubmissionDTO;
            }
            if (firstSolution == null && teamSubmissionDTO.isFirstSolved()) {
                firstSolution = teamSubmissionDTO;
            }
        }

        for (Boolean solved : teamMap.values()) {
            if (solved) {
                numProblemSolved++;
            } else {
                numProblemSubmitted++;
            }
            usedByNumTeams++;
        }
        report.setNumProblemSolved(numProblemSolved);
        report.setNumProblemSubmitted(numProblemSubmitted);
        report.setFirstSolution(firstSolution);
        report.setFirstSubmission(firstSubmission);
        report.setTotalSubmissions(teamProblems.size());
        report.setUsedByNumTeams(usedByNumTeams);
        report.setUsagePercentage(100 * teamProblems.size() / (double) totalSubmissions);

        return report.getFullReport();
    }
}
