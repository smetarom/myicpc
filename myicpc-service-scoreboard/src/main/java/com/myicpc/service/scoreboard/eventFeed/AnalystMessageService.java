package com.myicpc.service.scoreboard.eventFeed;

import com.myicpc.enums.NotificationType;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Team;
import com.myicpc.model.eventFeed.TeamProblem;
import com.myicpc.model.social.Notification;
import com.myicpc.service.notification.NotificationBuilder;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Roman Smetana
 */
@Service
public class AnalystMessageService {
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
        NotificationBuilder builder = new NotificationBuilder(teamProblem);
        builder.setTitle(getAnalystTitle(teamProblem));
        builder.setBody(message);
        builder.setNotificationType(notificationType);
        builder.setEntityId(teamProblem.getId());
        builder.setExternalId(String.valueOf(teamProblem.getTeam().getId()));
        builder.setTimestamp(new Date());
        builder.setContest(teamProblem.getTeam().getContest());
        return builder.build();
    }

    public Notification createTeamAnalyticsMessage(final Team team, String message) {
        NotificationBuilder builder = new NotificationBuilder(team);
        builder.setTitle(team.getName());
        builder.setBody(message);
        builder.setNotificationType(NotificationType.ANALYST_TEAM_MESSAGE);
        builder.setEntityId(team.getId());
        builder.setTimestamp(new Date());
        builder.setContest(team.getContest());
        return builder.build();
    }

    public Notification createAnalyticsMessage(final Contest contest, String message) {
        NotificationBuilder builder = new NotificationBuilder();
        builder.setTitle("Analytics message");
        builder.setBody(message);
        builder.setNotificationType(NotificationType.ANALYST_MESSAGE);
        builder.setTimestamp(new Date());
        builder.setContest(contest);
        return builder.build();
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
