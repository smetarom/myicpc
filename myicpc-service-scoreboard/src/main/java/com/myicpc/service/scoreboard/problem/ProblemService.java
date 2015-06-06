package com.myicpc.service.scoreboard.problem;

import com.google.gson.JsonArray;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Problem;
import com.myicpc.service.listener.ScoreboardListener;

import java.util.List;

/**
 * @author Roman Smetana
 */
public interface ProblemService extends ScoreboardListener {
    List<Problem> findByContest(Contest contest);

    JsonArray getSubmissionAttemptsJSON(Problem problem);
}
