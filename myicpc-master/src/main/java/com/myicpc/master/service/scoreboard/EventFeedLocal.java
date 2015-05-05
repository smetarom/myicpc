package com.myicpc.master.service.scoreboard;

import com.myicpc.dto.eventFeed.visitor.EventFeedVisitor;

import javax.ejb.Local;

/**
 * @author Roman Smetana
 */
@Local
public interface EventFeedLocal extends EventFeedVisitor {
}
