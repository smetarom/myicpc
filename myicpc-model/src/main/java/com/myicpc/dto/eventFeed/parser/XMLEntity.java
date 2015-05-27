package com.myicpc.dto.eventFeed.parser;

import com.myicpc.dto.eventFeed.visitor.EventFeedVisitor;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.EventFeedControl;

import java.io.Serializable;

/**
 * @author Roman Smetana
 */
public abstract class XMLEntity<T> implements Serializable {
    private Long contestId;

    public abstract void mergeTo(final T entity);

    public abstract void accept(EventFeedVisitor visitor, Contest contest);

    public Long getContestId() {
        return contestId;
    }

    public void setContestId(Long contestId) {
        this.contestId = contestId;
    }
}
