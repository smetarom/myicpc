package com.myicpc.service.scoreboard.eventFeed.dto;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.EventFeedControl;
import com.myicpc.model.eventFeed.Language;
import com.myicpc.service.scoreboard.eventFeed.EventFeedVisitor;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author Roman Smetana
 */
@XStreamAlias("language")
public class LanguageXML extends XMLEntity<Language> {
    private String name;

    @Override
    public void mergeTo(final Language language) {
        language.setName(getName());
    }

    @Override
    public void accept(EventFeedVisitor visitor, Contest contest, EventFeedControl eventFeedControl) {
        visitor.visit(this, contest, eventFeedControl);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
