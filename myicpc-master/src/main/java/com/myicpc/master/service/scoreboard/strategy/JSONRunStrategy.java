package com.myicpc.master.service.scoreboard.strategy;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.myicpc.commons.adapters.JSONAdapter;
import com.myicpc.commons.utils.FormatUtils;
import com.myicpc.commons.utils.WebServiceUtils;
import com.myicpc.master.exception.EventFeedException;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Team;
import com.myicpc.model.eventFeed.TeamProblem;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Roman Smetana
 */
@Stateless
public class JSONRunStrategy extends FeedRunStrategy {
    private static final Logger logger = LoggerFactory.getLogger(JSONRunStrategy.class);

    @Override
    protected List<Team> processScoreboardChanges(TeamProblem teamProblemFromDB, TeamProblem teamProblemFromFeed, Contest contest) throws EventFeedException {
        return synchronizeWithJSON(teamProblemFromDB, contest);
    }

    protected String getJSONContent(final Contest contest) throws EventFeedException {
        String jsonScoreboardURL = contest.getContestSettings().getJSONScoreboardURL();
        if (StringUtils.isEmpty(jsonScoreboardURL)) {
            throw new EventFeedException("Empty JSON scoreboard URL");
        }
        String username = contest.getContestSettings().getEventFeedUsername();
        String password = contest.getContestSettings().getEventFeedPassword();

        try {
            return IOUtils.toString(WebServiceUtils.connectCDS(jsonScoreboardURL, username, password), FormatUtils.DEFAULT_ENCODING);
        } catch (IOException e) {
            throw new EventFeedException(e.getMessage(), e);
        }
    }

    protected List<Team> synchronizeWithJSON(final TeamProblem teamProblemFromDB, final Contest contest) throws EventFeedException {
        try {
            // Get all teams
            Iterable<Team> teams = eventFeedDao.findTeamByContest(contest);

            // parse JSON scoreboard
            JsonObject root = new JsonParser().parse(getJSONContent(contest)).getAsJsonObject();

            JsonArray arr = root.getAsJsonArray("scoreboard");
            List<Team> teamsToBroadcast = new ArrayList<>();

            Iterator<JsonElement> jsonIterator = arr.iterator();
            // Mapping between team and its ID, for faster lookup
            Map<Long, Team> teamMap = new HashMap<>();
            for (Team team : teams) {
                teamMap.put(team.getSystemId(), team);
            }

            while (jsonIterator.hasNext()) {
                JSONAdapter jsonAdapter = new JSONAdapter(jsonIterator.next());
                Team team = teamMap.get(jsonAdapter.getLong("id"));

                // throw exception if team is not found by ID
                if (team == null) {
                    throw new EventFeedException("JSON event feed file contains a team, which is not in database.");
                }

                processTeamSubmission(team, teamProblemFromDB, jsonAdapter, teamsToBroadcast);

            }
            for (Team t : teams) {
                eventFeedDao.save(t);
            }
            return teamsToBroadcast;
        } catch (Throwable ex) {
            logger.error(ex.getMessage(), ex);
            throw new EventFeedException("Unknown problem with the parsing of the json scoreboard.", ex);
        }
    }

    protected void processTeamSubmission(final Team team, final TeamProblem teamProblemFromDB, final JSONAdapter teamAdapter,
                                         final List<Team> teamsToBroadcast) throws EventFeedException {
        int oldRank = team.getRank();
        team.setRank(teamAdapter.getInteger("rank"));

        // mark team, if the rank differs
        if (oldRank != team.getRank()) {
            teamsToBroadcast.add(team);
        }

        // this is the team, who submitted
        if (team.getId().equals(teamProblemFromDB.getTeam().getId())) {
            Integer solved = teamAdapter.getInteger("solved");
            Integer score = teamAdapter.getInteger("score");
            team.setProblemsSolved(solved);
            teamProblemFromDB.getTeam().setProblemsSolved(solved);
            team.setTotalTime(score);
            teamProblemFromDB.getTeam().setTotalTime(score);
            JsonElement elem = teamAdapter.get(teamProblemFromDB.getProblem().getCode());
            // problem by letter code is found
            if (elem != null) {
                JSONAdapter problemAdapter = new JSONAdapter(elem);
                teamProblemFromDB.setAttempts(problemAdapter.getInteger("a"));
                teamProblemFromDB.setTime(problemAdapter.getDouble("t") * 60);
                if ("first".equalsIgnoreCase(problemAdapter.getString("s"))) {
                    removeFirstSolvedProblem(teamProblemFromDB.getProblem());
                    teamProblemFromDB.setFirstSolved(true);
                }
            } else {
                throw new EventFeedException("Problem was not found");
            }
        }
    }

}
