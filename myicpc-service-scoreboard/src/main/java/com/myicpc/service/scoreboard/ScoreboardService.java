package com.myicpc.service.scoreboard;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Team;
import com.myicpc.model.eventFeed.TeamProblem;
import com.myicpc.service.listener.ScoreboardListener;

import java.util.List;

/**
 * @author Roman Smetana
 */
public interface ScoreboardService extends ScoreboardListener {
    @Override
    void onSubmission(TeamProblem teamProblem, List<Team> effectedTeams);

    JsonArray getTeamsFullTemplate(Contest contest);

    JsonArray getTeamsFullTemplate(Contest contest, List<Long> teamIds);

    JsonArray getTeamsScorebarTemplate(Contest contest);

    JsonObject getTeamMapCoordinates(Contest contest);
}
