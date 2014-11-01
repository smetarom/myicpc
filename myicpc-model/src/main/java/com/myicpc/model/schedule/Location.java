package com.myicpc.model.schedule;

import com.myicpc.model.IdGeneratedContestObject;
import edu.baylor.icpc.myicpc.view.validator.annotation.ValidateLocation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

/**
 * Location of the event
 *
 * @author smetana
 */
@ValidateLocation
@Entity
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "Location_id_seq")
public class Location extends IdGeneratedContestObject {
    private static final long serialVersionUID = 5736319309531520386L;

    /**
     * Name of the location
     */
    @NotNull
    private String name;
    /**
     * Code of the location used as a slug
     */
    @NotNull
    @Column(unique = true)
    private String code;

    /**
     * URL to embedded Google map
     */
    private String googleMapUrl;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getGoogleMapUrl() {
        return googleMapUrl;
    }

    public void setGoogleMapUrl(final String googleMapUrl) {
        this.googleMapUrl = googleMapUrl;
    }

    @Override
    public String toString() {
        return getName();
    }
}
