package com.myicpc.pubsub;

import org.atmosphere.config.service.DeliverTo;
import org.atmosphere.config.service.ManagedService;
import org.atmosphere.config.service.Ready;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.BroadcasterFactory;

import java.util.Objects;

/**
 * @author Roman Smetana
 */
@ManagedService(path = "{contestCode}/scoreboard")
public class ScoreboardResource {

//    @Ready
//    public String onReady(final AtmosphereResource r) {
//        Object o = BroadcasterFactory.getDefault().lookupAll();
//        return "{\"aho\": \"dassa\"}";
//    }
}
