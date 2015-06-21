package com.myicpc.dto.eventFeed.visitor;

import com.myicpc.dto.eventFeed.EventFeedEvent;
import com.myicpc.model.social.Notification;

/**
 * @author Roman Smetana
 */
public class EventFeedMessage implements EventFeedEvent {
    private final Notification notification;

    public EventFeedMessage(Notification notification) {
        this.notification = notification;
    }

    public Notification getNotification() {
        return notification;
    }
}
