package com.myicpc.service.scoreboard.eventFeed.dto;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.EventFeedControl;
import com.myicpc.service.scoreboard.eventFeed.EventFeedVisitor;

/**
 * @author Roman Smetana
 */
public abstract class XMLEntity<T> {
    public abstract void mergeTo(final T entity);

    public abstract void accept(EventFeedVisitor visitor, Contest contest, EventFeedControl eventFeedControl);
}
