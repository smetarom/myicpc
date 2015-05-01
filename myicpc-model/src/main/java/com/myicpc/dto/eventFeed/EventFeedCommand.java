package com.myicpc.dto.eventFeed;

import java.io.Serializable;

/**
 * @author Roman Smetana
 */
public class EventFeedCommand implements Serializable {
    private Long contestId;
    private boolean truncateDatabase;
    private boolean start;

    public EventFeedCommand() {
    }

    public EventFeedCommand(Long contestId, boolean truncateDatabase, boolean start) {
        this.contestId = contestId;
        this.truncateDatabase = truncateDatabase;
        this.start = start;
    }

    public Long getContestId() {
        return contestId;
    }

    public void setContestId(Long contestId) {
        this.contestId = contestId;
    }

    public boolean isTruncateDatabase() {
        return truncateDatabase;
    }

    public void setTruncateDatabase(boolean truncateDatabase) {
        this.truncateDatabase = truncateDatabase;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }
}
