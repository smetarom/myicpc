package com.myicpc.service.publish;

import com.google.gson.JsonObject;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
     * Broadcast a team submission to channel SCOREBOARD_CHANNEL
     */
    public void broadcastTeamProblem(final JsonObject teamProblemJSON, final String contestCode) {
        atmospherePublish(PREFIX + contestCode + "/" + SCOREBOARD_CHANNEL, teamProblemJSON.toString());
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
