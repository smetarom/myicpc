package com.myicpc.dto.eventFeed.parser;

import com.myicpc.dto.eventFeed.visitor.EventFeedVisitor;
import com.myicpc.model.contest.Contest;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Hack for tag 'reset' appended to Event feed
 *
 * @author Roman Smetana
 */
@XStreamAlias("reset")
public class ResetXML extends XMLEntity<Void> {
    @Override
    public void mergeTo(Void entity) {
        // ignore
    }

    @Override
    public void accept(EventFeedVisitor visitor, Contest contest) {
        // ignore
    }
}
