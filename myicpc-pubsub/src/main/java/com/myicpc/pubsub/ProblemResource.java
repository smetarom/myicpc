package com.myicpc.pubsub;

import org.atmosphere.config.service.ManagedService;
import org.atmosphere.plugin.redis.RedisBroadcaster;

/**
 * Atmosphere entry point for contest submissions on a problem
 *
 * This endpoint takes care about the WebSocket connections,
 * which are interested in subscribing to problem channel
 *
 * @author Roman Smetana
 */
@ManagedService(
        path = "{contestCode}/problem/{problemCode}",
        broadcaster = RedisBroadcaster.class
)
public class ProblemResource {
}
