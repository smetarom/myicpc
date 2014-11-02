package com.myicpc.service.publish;

import com.google.gson.JsonObject;
import com.myicpc.model.eventFeed.Team;
import com.myicpc.model.eventFeed.TeamProblem;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Roman Smetana
 */
@Service
public class PublishService {
    public static final Logger logger = LoggerFactory.getLogger(PublishService.class);
    /**
     * Web socket channel for submissions and contest related messages
     */
    public static final String CHANNEL_TEAM_PROBLEMS = "teamProblems";
    /**
     * Web socket channel for poll related messages
     */
    public static final String POLL = "poll";
    /**
     * Web socket channel for submissions and notification messages
     */
    public static final String NOTIFICATIONS = "notifications";
    /**
     * Web socket channel for Quest
     */
    public static final String QUEST = "quest";

    /**
     * Broadcast a team submission to channel CHANNEL_TEAM_PROBLEMS
     *
     * @param teamProblem      team submission which triggered an event
     * @param teamsToBroadcast all teams influenced by this team submission
     */
    public void broadcastTeamProblem(final TeamProblem teamProblem, final List<Team> teamsToBroadcast) {
        JsonObject tpObject = new JsonObject();
        tpObject.addProperty("type", "e");
        tpObject.addProperty("teamId", teamProblem.getTeam().getId());
        tpObject.addProperty("teamName", teamProblem.getTeam().getName());
        tpObject.addProperty("problemId", teamProblem.getProblem().getId());
        tpObject.addProperty("problemCode", teamProblem.getProblem().getCode());
        tpObject.addProperty("problemName", teamProblem.getProblem().getName());
        tpObject.addProperty("attempts", teamProblem.getAttempts());
        tpObject.addProperty("judged", teamProblem.getJudged());
        tpObject.addProperty("solved", teamProblem.getSolved());
        tpObject.addProperty("passed", teamProblem.getNumTestPassed());
        tpObject.addProperty("first", teamProblem.isFirstSolved());
        tpObject.addProperty("testcases", teamProblem.getProblem().getTotalTestcases());
        tpObject.addProperty("penalty", teamProblem.getPenalty());
        tpObject.addProperty("time", teamProblem.getTime());
        tpObject.addProperty("total", teamProblem.getTeam().getTotalTime());
        tpObject.addProperty("numSolved", teamProblem.getTeam().getProblemsSolved());
        tpObject.addProperty("oldRank", teamProblem.getOldRank());
        tpObject.addProperty("rank", teamProblem.getTeam().getRank());

        JsonObject teamsObject = new JsonObject();
        for (Team team : teamsToBroadcast) {
            JsonObject teamObject = new JsonObject();
            teamObject.addProperty("teamId", team.getId());
            teamObject.addProperty("rank", team.getRank());
            teamsObject.add(team.getId().toString(), teamObject);
        }
        tpObject.add("teams", teamsObject);

        atmospherePublish(CHANNEL_TEAM_PROBLEMS, tpObject.toString());
    }

    /**
     * Send a message to channel
     *
     * @param channel channel
     * @param message message
     */
    private void atmospherePublish(final String channel, final String message) {
        BroadcasterFactory bf = BroadcasterFactory.getDefault();
        if (bf != null) {
            Broadcaster b = bf.lookup(channel);
            if (b != null) {
                b.broadcast(message);
                return;
            }
        }
        logger.debug("Channel " + channel + " failed. Message not send: " + message);
    }
}
