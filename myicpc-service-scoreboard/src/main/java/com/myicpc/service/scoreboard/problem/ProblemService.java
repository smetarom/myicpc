package com.myicpc.service.scoreboard.problem;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Problem;
import com.myicpc.service.listener.ScoreboardListener;

import java.util.List;
import java.util.Map;

/**
 * Service responsible for {@link Problem} operations
 *
 * @author Roman Smetana
 */
public interface ProblemService extends ScoreboardListener {
    /**
     * Gets {@link Problem}s in {@code contest}
     *
     * @param contest contest
     * @return list of problems in the contest
     */
    List<Problem> findByContest(Contest contest);

    /**
     * Gets the report about teams and their submissions,
     * which submitted solutions for {@code problem}
     *
     * @param problem problem
     * @return JSON representation of team attempts
     */
    JsonArray getSubmissionAttemptsJSON(Problem problem);

    /**
     * Returns the judgment colors for all judgment in the contest
     *
     * @param contest contest
     * @return map between judgment code and color
     */
    Map<String, String> getJudgmentColors(Contest contest);

    /**
     * Gets all judgements in {@code contest}
     *
     * @param contest contest
     * @return JSON representation of all judgements in the contest
     */
    JsonObject getAllJudgementsJSON(Contest contest);

    /**
     * Gets the {@code problem} overview report
     *
     * Report contains the team and its last submissions on {@code problem}
     *
     * @param problem problem
     * @return JSON overview report
     */
    JsonArray getProblemOverviewJSON(Problem problem);
}
