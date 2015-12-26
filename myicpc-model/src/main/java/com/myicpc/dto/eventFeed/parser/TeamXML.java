package com.myicpc.dto.eventFeed.parser;

import com.myicpc.dto.eventFeed.visitor.EventFeedVisitor;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.teamInfo.Region;
import com.myicpc.model.eventFeed.Team;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author Roman Smetana
 */
@XStreamAlias("team")
public class TeamXML extends XMLEntity<Team> {
    @XStreamAlias("id")
    private Long id;

    @XStreamAlias("external-id")
    private Long externalId;

    private String name;

    private String nationality;

    @Override
    public void mergeTo(final Team team) {
        team.setName(getName());
        team.setSystemId(getId());
        team.setExternalId(getExternalId());
        team.setNationality(getNationality());
    }

    @Override
    public void accept(EventFeedVisitor visitor, Contest contest, EventFeedSettingsDTO eventFeedSettings) {
        visitor.visit(this, contest, eventFeedSettings);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getExternalId() {
        return externalId;
    }

    public void setExternalId(Long externalId) {
        this.externalId = externalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
}
