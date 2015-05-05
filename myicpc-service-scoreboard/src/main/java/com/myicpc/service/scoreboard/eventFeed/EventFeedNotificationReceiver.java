package com.myicpc.service.scoreboard.eventFeed;

import com.google.gson.JsonObject;
import com.myicpc.dto.eventFeed.EventFeedEvent;
import com.myicpc.dto.eventFeed.EventFeedSubmission;
import com.myicpc.dto.eventFeed.XMLEntity;
import com.myicpc.dto.eventFeed.visitor.EventFeedMessage;
import com.myicpc.dto.eventFeed.visitor.EventFeedVisitor;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Team;
import com.myicpc.model.eventFeed.TeamProblem;
import com.myicpc.repository.contest.ContestRepository;
import com.myicpc.service.publish.PublishService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Roman Smetana
 */
@Service
public class EventFeedNotificationReceiver {
    private static final Logger logger = LoggerFactory.getLogger(EventFeedNotificationReceiver.class);

    @Autowired
    private PublishService publishService;

    @JmsListener(destination = "java:/jms/queue/EventFeedQueue")
    public void processSocialNotification(EventFeedEvent event) {
        if (event instanceof EventFeedSubmission) {
            onSubmission((EventFeedSubmission) event);
        } else if (event instanceof EventFeedMessage) {
            // TODO process notifications
        }
    }

    public void onSubmission(final EventFeedSubmission eventFeedSubmission) {
        TeamProblem teamSubmission = eventFeedSubmission.getTeamSubmission();
        List<Team> effectedTeams = eventFeedSubmission.getChangedTeams();
        JsonObject tpObject = new JsonObject();
        tpObject.addProperty("type", "submission");
        tpObject.addProperty("teamId", teamSubmission.getTeam().getExternalId());
        tpObject.addProperty("teamName", teamSubmission.getTeam().getName());
        tpObject.addProperty("problemId", teamSubmission.getProblem().getId());
        tpObject.addProperty("problemCode", teamSubmission.getProblem().getCode());
        tpObject.addProperty("problemName", teamSubmission.getProblem().getName());
        tpObject.addProperty("attempts", teamSubmission.getAttempts());
        tpObject.addProperty("judged", teamSubmission.getJudged());
        tpObject.addProperty("solved", teamSubmission.getSolved());
        tpObject.addProperty("passed", teamSubmission.getNumTestPassed());
        tpObject.addProperty("first", teamSubmission.isFirstSolved());
        tpObject.addProperty("testcases", teamSubmission.getProblem().getTotalTestcases());
        tpObject.addProperty("penalty", teamSubmission.getPenalty());
        tpObject.addProperty("time", teamSubmission.getTime());
        tpObject.addProperty("total", teamSubmission.getTeam().getTotalTime());
        tpObject.addProperty("numSolved", teamSubmission.getTeam().getProblemsSolved());
        tpObject.addProperty("oldRank", teamSubmission.getOldRank());
        tpObject.addProperty("teamRank", teamSubmission.getTeam().getRank());

        JsonObject teamsObject = new JsonObject();
        for (Team team : effectedTeams) {
            JsonObject teamObject = new JsonObject();
            teamObject.addProperty("teamId", team.getExternalId());
            teamObject.addProperty("teamRank", team.getRank());
            teamsObject.add(team.getExternalId().toString(), teamObject);
        }
        tpObject.add("teams", teamsObject);

        publishService.broadcastTeamProblem(tpObject, teamSubmission.getTeam().getContest().getCode());
    }
}
