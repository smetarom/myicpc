package com.myicpc.service.scoreboard.dto.eventFeed;

import com.myicpc.service.scoreboard.dto.eventFeed.EventFeedControlDTO.EventFeedControlType;

import java.io.Serializable;

/**
 * The response to {@link EventFeedControlDTO} command
 *
 * @author Roman Smetana
 */
public class EventFeedControlResponseDTO implements Serializable {
    private static final long serialVersionUID = -4168692657185228625L;

    /**
     * The type of the action, which creates the response
     */
    private EventFeedControlType eventFeedControlType;

    public EventFeedControlResponseDTO(EventFeedControlType eventFeedControlType) {
        this.eventFeedControlType = eventFeedControlType;
    }

    public EventFeedControlType getEventFeedControlType() {
        return eventFeedControlType;
    }
}
