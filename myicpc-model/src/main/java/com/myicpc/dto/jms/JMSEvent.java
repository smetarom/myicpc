package com.myicpc.dto.jms;

import java.io.Serializable;

/**
 * @author Roman Smetana
 */
public class JMSEvent implements Serializable {
    private static final long serialVersionUID = 8974480822663361020L;

    public enum EventType {
        TWITTER, VINE, INSTAGRAM, POLL_OPEN, ADMIN_NOTIFICATION,
        QUEST_CHALLENGE, QUEST_SUBMISSIONS, QUEST_SUBMISSIONS_FULL,
        SCHEDULE_OPEN_EVENT, CM_SYNC_EVENT
    }

    private Long contestId;
    private EventType eventType;

    public JMSEvent() {
    }

    public JMSEvent(Long contestId, EventType eventType) {
        this.contestId = contestId;
        this.eventType = eventType;
    }

    public Long getContestId() {
        return contestId;
    }

    public void setContestId(Long contestId) {
        this.contestId = contestId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }
}