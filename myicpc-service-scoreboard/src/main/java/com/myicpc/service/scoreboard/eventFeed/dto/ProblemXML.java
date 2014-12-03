package com.myicpc.service.scoreboard.eventFeed.dto;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.EventFeedControl;
import com.myicpc.model.eventFeed.Problem;
import com.myicpc.service.scoreboard.eventFeed.EventFeedVisitor;
import com.myicpc.service.scoreboard.eventFeed.dto.convertor.ProblemCodeConverter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

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
