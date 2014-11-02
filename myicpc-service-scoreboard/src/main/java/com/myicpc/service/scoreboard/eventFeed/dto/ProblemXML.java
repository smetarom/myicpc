package com.myicpc.service.scoreboard.eventFeed.dto;

import com.myicpc.model.contest.Contest;
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
    @XStreamConverter(ProblemCodeConverter.class)
    private String code;

    private String name;

    @Override
    public void mergeTo(final Problem problem) {
        problem.setCode(getCode());
        problem.setName(getName());
    }

    @Override
    public void accept(EventFeedVisitor visitor, Contest contest) {
        visitor.visit(this, contest);
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
}