package com.myicpc.dto.eventFeed;

import com.myicpc.dto.eventFeed.visitor.EventFeedVisitor;
import com.myicpc.model.contest.Contest;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author Roman Smetana
 */
@XStreamAlias("clar")
public class ClarificationXML extends XMLEntity<Void> {

    @Override
    public void mergeTo(Void entity) {
        // do not merge clarification to entity
    }

    @Override
    public void accept(EventFeedVisitor visitor, Contest contest) {
        // do not process clarification
    }
}
