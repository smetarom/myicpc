package com.myicpc.service.scoreboard.eventFeed;

import com.myicpc.commons.utils.MessageUtils;
import com.myicpc.enums.NotificationType;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Team;
import com.myicpc.model.eventFeed.TeamProblem;
import com.myicpc.model.social.Notification;
import com.myicpc.repository.social.NotificationRepository;
import com.myicpc.service.listener.ScoreboardListenerAdapter;
import com.myicpc.service.notification.NotificationBuilder;
import com.myicpc.service.publish.PublishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Service for creating {@link Notification} for Event feed  submissions
 *
 * @author Roman Smetana
 */
@Service
public class AnalystMessageService extends ScoreboardListenerAdapter {
    @Autowired
    private PublishService publishService;

    @Autowired
    private NotificationRepository notificationRepository;

    /**
     * Generates a predefined message on solved submission
     * <p/>
     * It uses {@code scoreboard.solved.msg} and supplies it
     * with {@code teamProblem} parameters and publishes the created
     * notification
     *
     * @param teamProblem solved submission
     */
    @Override
    public void onSuccessSubmissionNotification(TeamProblem teamProblem) {
        String message = MessageUtils.getMessage("scoreboard.solved.msg",
                teamProblem.getTeam().getName(),
                teamProblem.getProblem().getCode(),
                teamProblem.getProblem().getName(),
                teamProblem.getTeam().getProblemsSolved(),
                teamProblem.getTeam().getRank());
        Notification notification = createOnTeamSubmissionSuccess(teamProblem, message);
        notification = notificationRepository.save(notification);
        publishService.broadcastNotification(notification, notification.getContest());
    }

    /**
     * Generates a predefined message on failed submission
     * <p/>
     * It uses {@code scoreboard.failed.msg} and supplies it
     * with {@code teamProblem} parameters and publishes the created
     * notification
     *
     * @param teamProblem failed submission
     */
    @Override
    public void onFailedSubmissionNotification(TeamProblem teamProblem) {
        String message = MessageUtils.getMessage("scoreboard.failed.msg",
                teamProblem.getTeam().getName(),
                teamProblem.getProblem().getCode(),
                teamProblem.getProblem().getName());
        Notification notification = createOnTeamSubmissionFailed(teamProblem, message);
        notification = notificationRepository.save(notification);
        publishService.broadcastNotification(notification, notification.getContest());
    }

    /**
     * Generates a predefined message on submitted submission
     * <p/>
     * It uses {@code scoreboard.submitted.msg} and supplies it
     * with {@code teamProblem} parameters and publishes the created
     * notification
     *
     * @param teamProblem submitted submission
     */
    @Override
    public void onPendingSubmissionNotification(TeamProblem teamProblem) {
        String message = MessageUtils.getMessage("scoreboard.submitted.msg",
                teamProblem.getTeam().getName(),
                teamProblem.getProblem().getCode(),
                teamProblem.getProblem().getName());
        Notification notification = createOnTeamSubmissionSubmitted(teamProblem, message);
        notification = notificationRepository.save(notification);
        publishService.broadcastNotification(notification, notification.getContest());
    }

    /**
     * Creates a {@link Notification} for solved submission with the supplied
     * notification body
     *
     * @param teamProblem solved submission
     * @param message     notification body
     * @return notification for solved submission
     */
    public Notification createOnTeamSubmissionSuccess(final TeamProblem teamProblem, String message) {
        return createOnTeamProblem(teamProblem, message, NotificationType.SCOREBOARD_SUCCESS);
    }

    /**
     * Creates a {@link Notification} for failed submission with the supplied
     * notification body
     *
     * @param teamProblem failed submission
     * @param message     notification body
     * @return notification for failed submission
     */
    public Notification createOnTeamSubmissionFailed(final TeamProblem teamProblem, String message) {
        return createOnTeamProblem(teamProblem, message, NotificationType.SCOREBOARD_FAILED);
    }

    /**
     * Creates a {@link Notification} for submitted submission with the supplied
     * notification body
     *
     * @param teamProblem submitted submission
     * @param message     notification body
     * @return notification for submitted submission
     */
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

    /**
     * Creates a {@link Notification} for a message about {@code team}
     *
     * @param team    team mentioned in the message
     * @param message message about a team
     * @return notification about team message
     */
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

    /**
     * Creates a {@link Notification} for a message about {@code contest}
     *
     * @param contest contest mentioned in the message
     * @param message message about a contest
     * @return notification about contest message
     */
    public Notification createAnalyticsMessage(final Contest contest, String message) {
        NotificationBuilder builder = new NotificationBuilder();
        builder.setTitle("Analytics message");
        builder.setBody(message);
        builder.setNotificationType(NotificationType.ANALYST_MESSAGE);
        builder.setTimestamp(new Date());
        builder.setContest(contest);
        return builder.build();
    }

    private static String getAnalystTitle(final TeamProblem teamProblem) {
        String key;
        if (teamProblem.getJudged()) {
            if (teamProblem.getSolved()) {
                key = "scoreboard.solved.title";
            } else {
                key = "scoreboard.failed.title";
            }
        } else {
            key = "scoreboard.submitted.title";
        }
        return MessageUtils.getMessage(key, teamProblem.getTeam().getName(), teamProblem.getProblem().getCode(), teamProblem.getProblem().getName());
    }
}
