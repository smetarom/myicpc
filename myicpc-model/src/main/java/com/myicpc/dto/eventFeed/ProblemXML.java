package com.myicpc.dto.eventFeed;

import com.myicpc.dto.eventFeed.visitor.EventFeedVisitor;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.EventFeedControl;
import com.myicpc.model.eventFeed.Problem;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author Roman Smetana
 */
@XStreamAlias("problem")
public class ProblemXML extends XMLEntity<Problem> {
    @XStreamAlias("id")
    private Long systemId;

    private String code;

    private String name;

    @Override
    public void mergeTo(final Problem problem) {
        problem.setName(getName());
        problem.setSystemId(getSystemId());
    }

    @Override
    public void accept(EventFeedVisitor visitor, Contest contest, EventFeedControl eventFeedControl) {
        visitor.visit(this, contest, eventFeedControl);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }
}
