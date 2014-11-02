package com.myicpc.pubsub;

import org.atmosphere.client.TrackMessageSizeInterceptor;
import org.atmosphere.config.service.AtmosphereService;
import org.atmosphere.cpr.BroadcasterFactory;
import org.atmosphere.interceptor.AtmosphereResourceLifecycleInterceptor;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * @author Roman Smetana
 */
@Path("/notifications")
@AtmosphereService(
        dispatch = false,
        interceptors = {AtmosphereResourceLifecycleInterceptor.class, TrackMessageSizeInterceptor.class},
        path = "/notifications",
        servlet = "org.glassfish.jersey.servlet.ServletContainer")
public class NotificationResource {

    @POST
    public void broadcast(String message) {
        BroadcasterFactory.getDefault().lookup("/notifications").broadcast(message);
    }
}
