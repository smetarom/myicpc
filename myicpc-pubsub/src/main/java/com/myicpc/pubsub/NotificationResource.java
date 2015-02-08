package com.myicpc.pubsub;

import org.atmosphere.config.service.ManagedService;

/**
 * Atmosphere entry point for notification
 *
 * This endpoint takes care about the WebSocket connections,
 * which are interested in subscribing to notification channel
 *
 * @author Roman Smetana
 */
@ManagedService(path = "{contestCode}/notification")
public class NotificationResource {
}
