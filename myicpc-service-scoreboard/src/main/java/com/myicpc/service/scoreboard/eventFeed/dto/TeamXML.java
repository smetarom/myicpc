package com.myicpc.service.scoreboard.eventFeed.dto;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.EventFeedControl;
import com.myicpc.model.eventFeed.Region;
import com.myicpc.model.eventFeed.Team;
import com.myicpc.service.scoreboard.eventFeed.EventFeedVisitor;
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

    @XStreamAlias("university")
    private String universityName;

    private Region region;

    @Override
    public void mergeTo(final Team team) {
        team.setName(getName());
        team.setSystemId(getId());
        team.setExternalId(getExternalId());
        team.setNationality(getNationality());
        team.setUniversityName(getUniversityName());
        team.setRegion(getRegion());
    }

    @Override
    public void accept(EventFeedVisitor visitor, Contest contest, EventFeedControl eventFeedControl) {
        visitor.visit(this, contest, eventFeedControl);
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

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }
}
