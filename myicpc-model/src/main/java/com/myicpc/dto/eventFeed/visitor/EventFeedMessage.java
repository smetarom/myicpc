package com.myicpc.dto.eventFeed.visitor;

import com.myicpc.dto.eventFeed.EventFeedEvent;

/**
 * @author Roman Smetana
 */
public class EventFeedMessage implements EventFeedEvent {
    private Long runId;
    private Long teamId;
    private String title;
    private String message;

    public EventFeedMessage(Long runId, Long teamId, String title, String message) {
        this.runId = runId;
        this.teamId = teamId;
        this.title = title;
        this.message = message;
    }

    public Long getRunId() {
        return runId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }
}
