package com.myicpc.dto.eventFeed.parser;

import com.myicpc.dto.eventFeed.visitor.EventFeedVisitor;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Judgement;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author Roman Smetana
 */
@XStreamAlias("judgement")
public class JudgementXML extends XMLEntity<Judgement> {
    private String acronym;

    private String name;

    @Override
    public void mergeTo(Judgement judgement) {
        judgement.setName(getName());
        judgement.setCode(getAcronym());
    }

    @Override
    public void accept(EventFeedVisitor visitor, Contest contest, EventFeedSettingsDTO eventFeedSettings) {
        visitor.visit(this, contest, eventFeedSettings);
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
