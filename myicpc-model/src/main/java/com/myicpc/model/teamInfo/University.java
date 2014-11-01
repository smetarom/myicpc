package com.myicpc.model.teamInfo;

import com.myicpc.model.IdGeneratedObject;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Represents university which team(s) participate in contest
 * <p/>
 * This is mirror of Institution from CM
 *
 * @author Roman Smetana
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"externalId"}))
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "University_id_seq")
public class University extends IdGeneratedObject {
    private static final long serialVersionUID = 2166770559486231031L;

    /**
     * Institution alias id from CM
     */
    private Long externalId;
    /**
     * University name
     */
    private String name;
    /**
     * University short name
     */
    private String shortName;
    /**
     * Hashtag for university
     */
    private String twitterHash;
    /**
     * University homepage
     */
    private String homepageURL;
    /**
     * State, if applicable, of the university
     */
    private String state;
    /**
     * Country of the university
     */
    private String country;

    public Long getExternalId() {
        return externalId;
    }

    public void setExternalId(final Long externalId) {
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

    public String getTwitterHash() {
        return twitterHash;
    }

    public void setTwitterHash(final String twitterHash) {
        this.twitterHash = twitterHash;
    }

    public String getHomepageURL() {
        return homepageURL;
    }

    public void setHomepageURL(final String homepageURL) {
        this.homepageURL = homepageURL;
    }

    public String getState() {
        return state;
    }

    public void setState(final String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }
}
