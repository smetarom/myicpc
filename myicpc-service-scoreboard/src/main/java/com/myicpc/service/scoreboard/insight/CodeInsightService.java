package com.myicpc.service.scoreboard.insight;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.myicpc.commons.adapters.JSONAdapter;
import com.myicpc.commons.utils.WebServiceUtils;
import com.myicpc.model.codeInsight.CodeInsightActivity;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Problem;
import com.myicpc.repository.codeInsight.CodeInsightActivityRepository;
import com.myicpc.repository.contest.ContestRepository;
import com.myicpc.repository.eventFeed.LanguageRepository;
import com.myicpc.repository.eventFeed.ProblemRepository;
import com.myicpc.repository.eventFeed.TeamRepository;
import com.myicpc.service.contest.ContestService;
import com.myicpc.service.scoreboard.dto.insight.CodeInsightProblem;
import com.myicpc.service.scoreboard.dto.insight.CodeInsightSnapshot;
import com.myicpc.service.scoreboard.dto.insight.CodeInsightTeam;
import com.myicpc.service.scoreboard.exception.CodeInsightException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import twitter4j.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Code insight service
 *
 * This service is responsible for business logic related to insight into team source codes
 *
 * @author Roman Smetana
 */
@Service
@Transactional
public class CodeInsightService {
    private static final Logger logger = LoggerFactory.getLogger(CodeInsightService.class);
    /**
     * Map between contest time in minutes and {@code CodeInsightSnapshot}
     */
    private static final ConcurrentMap<Integer, CodeInsightSnapshot> cachedSnapshots = new ConcurrentHashMap<>();
    /**
     * The size of time intervals in minutes
     *
     * All insight activities are grouped by problem
     */
    private static final int INSIGHT_INTERVAL = 5;
    /**
     * Number of code insight intervals calculated and displayed in UI
     */
    private static final int INSIGHT_HISTORY = 5;
    /**
     * Number of minutes, code insight calculation goes back
     */
    private static final int INSIGHT_HISTORY_MINUTES = INSIGHT_HISTORY * INSIGHT_INTERVAL;

    /**
     * The aspect, code insight service is interested
     */
    public enum InsideCodeMode {
        /**
         * Number of changed line between two subsequent file versions
         */
        DIFF,
        /**
         * The total number of lines of the file
         */
        COUNT
    }

    @Autowired
    private ContestService contestService;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private CodeInsightActivityRepository codeInsightActivityRepository;

    /**
     * Processes the code insight update from remote web service
     *
     * It parses the response and creates {@link CodeInsightActivity}s, if the activity does not exist, or skips the activity,
     * if it has been already seen.
     *
     * @param codeInsightResponse JSON web service response
     * @param contest contest
     * @throws CodeInsightException the received JSON response is not valid
     */
    public void processCodeInsightResource(final String codeInsightResponse, final Contest contest) throws CodeInsightException {
        if (StringUtils.isEmpty(codeInsightResponse) || "[]".equals(codeInsightResponse)) {
            // skip if result is empty
            return;
        }

        try {
            JsonArray arr = new JsonParser().parse(codeInsightResponse).getAsJsonArray();
            // parse each edit activity record
            for (JsonElement e : arr) {
                try {
                    // JsonObject object = e.getAsJsonObject();
                    JSONAdapter recordAdapter = new JSONAdapter(e);
                    Long externalId = recordAdapter.getLong("id");

                    CodeInsightActivity codeInsightActivity = codeInsightActivityRepository.findByExternalId(externalId);
                    if (codeInsightActivity != null) {
                        continue;
                    }
                    codeInsightActivity = new CodeInsightActivity();
                    codeInsightActivity.setExternalId(externalId);
                    codeInsightActivity.setDiffLineCount(recordAdapter.getInteger("diff_line_count"));
                    codeInsightActivity.setLineCount(recordAdapter.getInteger("line_count"));
                    codeInsightActivity.setFileSize(recordAdapter.getInteger("file_size_bytes"));
                    codeInsightActivity.setModifyTime(recordAdapter.getInteger("time"));
                    codeInsightActivity.setLanguage(languageRepository.findByNameIgnoreCase(recordAdapter.getString("language")));
                    codeInsightActivity.setTeam(teamRepository.findBySystemIdAndContest(recordAdapter.getLong("team_id"), contest));
                    codeInsightActivity.setProblem(problemRepository.findByCodeIgnoreCaseAndContest(recordAdapter.getString("problem_id"), contest));

                    codeInsightActivityRepository.save(codeInsightActivity);
                } catch (Throwable ex) {
                    logger.warn("Code insight was not processed." + codeInsightResponse, ex);
                }
            }
        } catch (JsonSyntaxException ex) {
            throw new CodeInsightException("Invalid code insight response: \n" + codeInsightResponse, ex);
        }
    }

    /**
     * Calculates code insight report
     *
     * @param contest contest
     * @param insideCodeMode aspect of code insight
     * @return JSON representation of code insight report
     */
    public JsonObject createCodeInsightReport(final Contest contest, final InsideCodeMode insideCodeMode) {
        int contestTime = (int) (contestService.getCurrentContestTime(contest) / 60);
        int historyTime = Math.max(contestTime - INSIGHT_HISTORY_MINUTES, 0);

        List<Problem> problems = problemRepository.findByContestOrderByCodeAsc(contest);
        JsonObject root = new JsonObject();
        // creates code insight for each problem
        for (Problem problem : problems) {
            Set<CodeInsightTeam> activeTeams = new LinkedHashSet<>();

            // retrieve all teams, which created activities in period between now and historyTime
            for (int time = contestTime; time >= historyTime; time -= INSIGHT_INTERVAL) {
                CodeInsightProblem insightProblem = getProblemFromSnapshot(time, problem, problems);
                activeTeams.addAll(insightProblem.getTeamsSorted(insideCodeMode));
                if (activeTeams.size() > INSIGHT_INTERVAL) {
                    break;
                }
            }
            if (!activeTeams.isEmpty()) {
                List<CodeInsightTeam> sortedTeams = new ArrayList<>(activeTeams);
                // gets top INSIGHT_HISTORY teams
                sortedTeams = sortedTeams.subList(0, Math.min(sortedTeams.size(), INSIGHT_HISTORY));

                JsonArray problemArray = new JsonArray();
                for (int time = contestTime; time >= historyTime; time -= INSIGHT_INTERVAL) {
                    CodeInsightProblem insightProblem = getProblemFromSnapshot(time, problem, problems);

                    JsonObject pair = new JsonObject();
                    pair.addProperty("key", time);
                    JsonArray data = new JsonArray();
                    for (CodeInsightTeam sortedTeam : sortedTeams) {
                        CodeInsightTeam team = insightProblem.getTeamById(sortedTeam.getTeam().getId());
                        JsonArray teamArray = new JsonArray();
                        teamArray.add(new JsonPrimitive(sortedTeam.getTeamLabel()));
                        if (team != null) {
                            teamArray.add(new JsonPrimitive(team.getChartData(insideCodeMode)));
                        } else {
                            teamArray.add(new JsonPrimitive(0));
                        }
                        data.add(teamArray);
                    }
                    pair.add("values", data);
                    problemArray.add(pair);
                }
                root.add(problem.getCode(), problemArray);
            }
        }

        // TODO clear old codeInsightSnapshot from the cache (map)
        return root;
    }

    private CodeInsightProblem getProblemFromSnapshot(int time, Problem problem, final List<Problem> problems) {
        CodeInsightSnapshot snapshot = getSnapshot(time, problems);
        return snapshot.getProblemById(problem.getId());
    }

    private CodeInsightSnapshot getSnapshot(int time, final List<Problem> problems) {
        CodeInsightSnapshot codeInsightSnapshot = cachedSnapshots.get(time);
        if (codeInsightSnapshot == null) {
            codeInsightSnapshot = createSnapshotAt(time, problems);
            cachedSnapshots.put(time, codeInsightSnapshot);
        }
        return codeInsightSnapshot;
    }

    private CodeInsightSnapshot createSnapshotAt(int time, final List<Problem> problems) {
        List<CodeInsightActivity> activities = codeInsightActivityRepository.findByModifyTimeBetween(time - INSIGHT_INTERVAL, time);

        Map<Long, CodeInsightProblem> problemMap = new HashMap<>();
        for (Problem problem : problems) {
            CodeInsightProblem insightProblem = new CodeInsightProblem();
            problemMap.put(problem.getId(), insightProblem);
        }

        for (CodeInsightActivity activity : activities) {
            CodeInsightProblem insightProblem = problemMap.get(activity.getProblem().getId());
            if (insightProblem == null) {
                insightProblem = new CodeInsightProblem();
                problemMap.put(activity.getProblem().getId(), insightProblem);
            }
            insightProblem.addTeamActivity(activity);
        }

        return new CodeInsightSnapshot(time, problemMap);
    }
}
