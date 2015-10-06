package com.myicpc.service.scoreboard;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Team;
import com.myicpc.model.eventFeed.TeamProblem;
import com.myicpc.service.listener.ScoreboardListener;

import java.util.List;

/**
 * Service responsible for the scoreboard and other scoreboard views such as scorebar or map
 *
 * @author Roman Smetana
 */
public interface ScoreboardService extends ScoreboardListener {
    /**
     * Returns the JSON model of scoreboard
     *
     * @param contest contest
     * @return scoreboard JSON model
     */
    JsonArray getTeamsFullTemplate(Contest contest);

    /**
     * Returns the JSON model of scorebar
     *
     * @param contest contest
     * @return scorebar JSON model
     */
    JsonArray getTeamsScorebarTemplate(Contest contest);

    /**
     * Returns map coordinates(longitude and latitude) for all teams in {@code contest}
     *
     * @param contest contest
     * @return teams map coordinates
     */
    JsonObject getTeamMapCoordinates(Contest contest);

    /**
     * Returns the JSON model of all problems in {@code contest}
     *
     * @param contest contest
     * @return JSON model of contest problems
     */
    JsonArray getProblemsJSON(Contest contest);
}
