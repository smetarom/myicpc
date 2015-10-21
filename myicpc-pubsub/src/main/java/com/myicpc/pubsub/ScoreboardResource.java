package com.myicpc.pubsub;

import org.atmosphere.config.service.ManagedService;
import org.atmosphere.plugin.redis.RedisBroadcaster;

/**
 * Atmosphere entry point for contest submissions and contest related events
 *
 * This endpoint takes care about the WebSocket connections,
 * which are interested in subscribing to scoreboard channel
 *
 * @author Roman Smetana
 */
@ManagedService(
        path = "pubsub/{contestCode}/scoreboard",
        broadcaster = RedisBroadcaster.class
)
public class ScoreboardResource {
}
