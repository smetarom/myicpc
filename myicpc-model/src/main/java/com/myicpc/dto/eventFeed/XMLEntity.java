package com.myicpc.dto.eventFeed;

import com.myicpc.dto.eventFeed.visitor.EventFeedVisitor;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.EventFeedControl;

import java.io.Serializable;

/**
 * @author Roman Smetana
 */
public abstract class XMLEntity<T> implements Serializable {
    public abstract void mergeTo(final T entity);

    public abstract void accept(EventFeedVisitor visitor, Contest contest, EventFeedControl eventFeedControl);
}
