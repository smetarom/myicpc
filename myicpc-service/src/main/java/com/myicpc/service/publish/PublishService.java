package com.myicpc.service.publish;

import com.google.gson.JsonObject;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.TeamProblem;
import com.myicpc.model.poll.Poll;
import com.myicpc.model.poll.PollOption;
import com.myicpc.model.social.Notification;
import com.myicpc.service.notification.NotificationService;
import org.atmosphere.cpr.AtmosphereFramework;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.atmosphere.cpr.MetaBroadcaster;
import org.atmosphere.util.ServletContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;

/**
 * @author Roman Smetana
 */
@Service
public class PublishService {
    public static final Logger logger = LoggerFactory.getLogger(PublishService.class);

    private static final String PREFIX = "/pubsub/";
    /**
     * Web socket channel for submissions and contest related messages
     */
    public static final String SCOREBOARD_CHANNEL = "scoreboard";
    /**
     * Web socket channel for submissions and contest related messages
     */
    public static final String PROBLEM_CHANNEL = "problem";
    /**
     * Web socket channel for poll related messages
     */
    public static final String POLL = "poll";
    /**
     * Web socket channel for submissions and notification messages
     */
    public static final String NOTIFICATION = "notification";
    /**
     * Web socket channel for Quest
     */
    public static final String QUEST = "quest";

    /**
     * Broadcast a team submission to channel SCOREBOARD_CHANNEL
     */
    public void broadcastTeamProblem(final JsonObject teamProblemJSON, final String contestCode) {
        atmospherePublish(PREFIX + contestCode + "/" + SCOREBOARD_CHANNEL, teamProblemJSON.toString());
    }

    /**
     * Broadcast the notification to notification channel
     *
     * @param notification
     *            notification, which triggered the event
     */
    public void broadcastNotification(final Notification notification, final Contest contest) {
        if (notification.isOffensive()) {
            // ignore offensive notifications
            return;
        }
        JsonObject notificationObject = NotificationService.getNotificationInJson(notification);

        atmospherePublish(PREFIX + contest.getCode() + "/" + NOTIFICATION, notificationObject.toString());
    }

    /**
     * Broadcast the contest submissions on a problem to the problem channel
     *
     * @param teamSubmission
     *            submission, which triggered the event
     */
    public void broadcastProblem(JsonObject teamSubmission, String problemCode, String contestCode) {
        atmospherePublish(PREFIX + contestCode + "/" + PROBLEM_CHANNEL + "/" + problemCode,
                teamSubmission.toString());
    }

    /**
     * Broadcast the poll updates
     *
     * @param poll
     * @param option
     */
    public void broadcastPollAnswer(final Poll poll, final PollOption option) {
        JsonObject pollUpdate = new JsonObject();
        pollUpdate.addProperty("pollId", poll.getId());
        pollUpdate.addProperty("optionId", option.getId());
        pollUpdate.addProperty("optionName", option.getName());
        pollUpdate.addProperty("votes", option.getVotes());
        atmospherePublish(PREFIX + poll.getContest().getCode() + "/" + POLL, pollUpdate.toString());
    }

    /**
     * Send a message to channel
     *
     * @param channel channel
     * @param message message
     */
    private void atmospherePublish(final String channel, final String message) {
//        ServletContext servletContext = ServletContextFactory.getDefault().getServletContext();
//        AtmosphereFramework framework = (AtmosphereFramework) servletContext.getAttribute("AtmosphereServlet");
//        BroadcasterFactory broadcasterFactory = framework.getBroadcasterFactory();
//        System.out.println(broadcasterFactory);
        BroadcasterFactory broadcasterFactory = BroadcasterFactory.getDefault();
        if (broadcasterFactory != null) {
            Broadcaster b = broadcasterFactory.lookup(channel);
            if (b != null) {
                b.broadcast(message);
                return;
            }
        }
        logger.debug("Channel " + channel + " failed. Message not send: " + message);
    }
}
