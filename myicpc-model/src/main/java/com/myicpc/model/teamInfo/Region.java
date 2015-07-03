package com.myicpc.model.teamInfo;

import com.myicpc.model.IdGeneratedObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;

/**
 * Geographical unit to group teams
 *
 * @author Roman Smetana
 */
@Entity
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "Region_id_seq")
public class Region extends IdGeneratedObject {
    private static final long serialVersionUID = 4894738108560406124L;

    /**
     * Region id from CM
     */
    @Column(unique = true)
    private Long externalId;
    /**
     * Region name
     */
    private String name;
    /**
     * Abbreviated region name
     */
    private String shortName;

    public Long getExternalId() {
        return externalId;
    }

    public void setExternalId(Long externalId) {
        this.externalId = externalId;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(final String shortName) {
        this.shortName = shortName;
    }
}
