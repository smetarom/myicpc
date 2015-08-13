package com.myicpc.service.scoreboard.dto.eventFeed;

import com.myicpc.model.contest.Contest;

import java.io.Serializable;
import java.util.Date;

/**
 * The command, which is send to receiver and the receiver executes the action
 * based on {@link #eventFeedControlType}
 *
 * @author Roman Smetana
 */
public class EventFeedControlDTO implements Serializable {
    private static final long serialVersionUID = -7255729906994096460L;

    /**
     * Available actions for {@link EventFeedControlDTO}
     */
    public enum EventFeedControlType {
        STOP, STATUS
    }

    /**
     * Type of the actione invoked by the command
     */
    private EventFeedControlType eventFeedControlType;
    /**
     * Date when the command was created
     */
    private Date submittedDate;
    /**
     * {@link Contest#id}, for what contest the command is created
     */
    private Long contestId;

    public EventFeedControlDTO(EventFeedControlType eventFeedControlType, Date submittedDate, Long contestId) {
        this.eventFeedControlType = eventFeedControlType;
        this.submittedDate = submittedDate;
        this.contestId = contestId;
    }

    public EventFeedControlType getEventFeedControlType() {
        return eventFeedControlType;
    }

    public Date getSubmittedDate() {
        return submittedDate;
    }

    public Long getContestId() {
        return contestId;
    }
}
