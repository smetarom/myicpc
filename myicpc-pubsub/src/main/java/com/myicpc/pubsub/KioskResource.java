package com.myicpc.pubsub;

import org.atmosphere.config.service.ManagedService;
import org.atmosphere.plugin.redis.RedisBroadcaster;

/**
 * Atmosphere entry point for kiosk related events
 *
 * This endpoint takes care about the WebSocket connections,
 * which are interested in subscribing to kiosk channel
 *
 * @author Roman Smetana
 */
@ManagedService(
        path = "{contestCode}/kiosk",
        broadcaster = RedisBroadcaster.class
)
public class KioskResource {
}
