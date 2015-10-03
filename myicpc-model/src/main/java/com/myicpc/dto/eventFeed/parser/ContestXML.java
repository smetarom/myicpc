package com.myicpc.dto.eventFeed.parser;

import com.myicpc.dto.eventFeed.convertor.TimeConverter;
import com.myicpc.dto.eventFeed.convertor.TimestampConverter;
import com.myicpc.dto.eventFeed.visitor.EventFeedVisitor;
import com.myicpc.model.contest.Contest;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import java.util.Date;

/**
 * @author Roman Smetana
 */
@XStreamAlias("info")
public class ContestXML extends XMLEntity<Contest> {
    private String title;

    @XStreamConverter(TimestampConverter.class)
    @XStreamAlias("starttime")
    private Date startTime;

    @XStreamConverter(TimeConverter.class)
    private int length;

    private double penalty;

    @Override
    public void mergeTo(final Contest contest) {
        contest.setPenalty(getPenalty());
        contest.setStartTime(getStartTime());
        contest.setLength(getLength());
    }

    @Override
    public void accept(EventFeedVisitor visitor, Contest contest) {
        visitor.visit(this, contest);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public double getPenalty() {
        return penalty;
    }

    public void setPenalty(double penalty) {
        this.penalty = penalty;
    }
}
