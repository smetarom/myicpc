package com.myicpc.pubsub;

import org.atmosphere.config.service.ManagedService;
import org.atmosphere.plugin.redis.RedisBroadcaster;

/**
 * Atmosphere entry point for notification
 *
 * This endpoint takes care about the WebSocket connections,
 * which are interested in subscribing to notification channel
 *
 * @author Roman Smetana
 */
@ManagedService(
        path = "pubsub/{contestCode}/poll",
        broadcaster = RedisBroadcaster.class
)
public class PollResource {
}
