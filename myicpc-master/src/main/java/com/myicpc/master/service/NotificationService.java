package com.myicpc.master.service;

import com.myicpc.enums.NotificationType;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Team;
import com.myicpc.model.eventFeed.TeamProblem;
import com.myicpc.model.social.Notification;

import javax.ejb.Stateless;
import java.util.Date;

/**
 * @author Roman Smetana
 */
@Stateless
public class NotificationService {
    public Notification createOnTeamSubmissionSuccess(final TeamProblem teamProblem, String message) {
        return createOnTeamProblem(teamProblem, message, NotificationType.SCOREBOARD_SUCCESS);
    }

    public Notification createOnTeamSubmissionFailed(final TeamProblem teamProblem, String message) {
        return createOnTeamProblem(teamProblem, message, NotificationType.SCOREBOARD_FAILED);
    }

    public Notification createOnTeamSubmissionSubmitted(final TeamProblem teamProblem, String message) {
        return createOnTeamProblem(teamProblem, message, NotificationType.SCOREBOARD_SUBMIT);
    }

    private Notification createOnTeamProblem(final TeamProblem teamProblem, final String message, final NotificationType notificationType) {
        Notification notification = new Notification();
        notification.setTitle(getAnalystTitle(teamProblem));
        notification.setBody(message);
        notification.setNotificationType(notificationType);
        notification.setEntityId(teamProblem.getId());
        notification.setExternalId(String.valueOf(teamProblem.getTeam().getId()));
        notification.setTimestamp(new Date());
        notification.setContest(teamProblem.getTeam().getContest());
        return notification;
    }

    public Notification createTeamAnalyticsMessage(final Team team, String message) {
        Notification notification = new Notification();
        notification.setTitle(team.getName());
        notification.setBody(message);
        notification.setNotificationType(NotificationType.ANALYST_TEAM_MESSAGE);
        notification.setEntityId(team.getId());
        notification.setTimestamp(new Date());
        notification.setContest(team.getContest());
        return notification;
    }

    public Notification createAnalyticsMessage(final Contest contest, String message) {
        Notification notification = new Notification();
        notification.setTitle("Analytics message");
        notification.setBody(message);
        notification.setNotificationType(NotificationType.ANALYST_MESSAGE);
        notification.setTimestamp(new Date());
        notification.setContest(contest);
        return notification;
    }

    public String getAnalystTitle(final TeamProblem teamProblem) {
        String key;
        if (teamProblem.getJudged()) {
            if (teamProblem.getSolved()) {
                key = "%s solved %s";
            } else {
                key = "%s failed %s";
            }
        } else {
            key = "%s submitted %s";
        }
        return String.format(key, teamProblem.getTeam().getName(), teamProblem.getProblem().getCode());
    }
}
