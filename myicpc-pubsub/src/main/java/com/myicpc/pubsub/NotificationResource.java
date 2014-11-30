package com.myicpc.pubsub;

import org.atmosphere.config.service.DeliverTo;
import org.atmosphere.config.service.ManagedService;
import org.atmosphere.config.service.PathParam;
import org.atmosphere.config.service.Ready;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.BroadcasterFactory;

/**
 * @author Roman Smetana
 */
@ManagedService(path = "{contestCode}/notification")
public class NotificationResource {
}
