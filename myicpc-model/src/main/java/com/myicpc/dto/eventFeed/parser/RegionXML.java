package com.myicpc.dto.eventFeed.parser;

import com.myicpc.dto.eventFeed.visitor.EventFeedVisitor;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.teamInfo.Region;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author Roman Smetana
 */
@XStreamAlias("region")
public class RegionXML extends XMLEntity<Region> {
    @XStreamAlias("external-id")
    private Long externalId;

    private String name;

    @Override
    public void mergeTo(final Region region) {
        region.setExternalId(getExternalId());
        region.setName(getName());
    }

    @Override
    public void accept(EventFeedVisitor visitor, Contest contest) {
        visitor.visit(this, contest);
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
}
