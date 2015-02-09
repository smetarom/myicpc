package com.myicpc.model.schedule;

import com.myicpc.model.StartEndDateObject;
import com.myicpc.validator.annotation.ValidateDateRange;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents an event in the schedule
 *
 * @author Roman Smetana
 */
@Entity
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "Event_id_seq")
@ValidateDateRange
public class Event extends StartEndDateObject {
    private static final long serialVersionUID = -4924079934694291712L;

    /**
     * Name of the event
     */
    @NotNull
    private String name;
    /**
     * Detailed description of the event
     */
    @Lob
    @Type(type = "org.hibernate.type.StringClobType")
    private String description;
    /**
     * Description of things, people should bring to the event
     */
    @Lob
    @Type(type = "org.hibernate.type.StringClobType")
    private String thingsToBring;
    /**
     * Event code used as a slug
     */
    @NotNull
    @Column(unique = true)
    private String code;
    /**
     * Event social hashtag
     */
    private String hashtag;
    /**
     * Picasa hashtag to the gallery
     */
    private String picasaTag;

    /**
     * Location (venue) of the event
     */
    @ManyToOne
    @JoinColumn(name = "locationId")
    private Location location;
    /**
     * To which contest day the event belongs
     */
    @NotNull
    @ManyToOne
    @JoinColumn(name = "scheduleDayId", nullable = false)
    private ScheduleDay scheduleDay;

    /**
     * List of roles, which are invited for the event
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "EventRoleEventAssociation", joinColumns = @JoinColumn(name = "eventId", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "eventRoleId", referencedColumnName = "id"))
    private Set<EventRole> roles = new HashSet<EventRole>();

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getThingsToBring() {
        return thingsToBring;
    }

    public void setThingsToBring(final String thingsToBring) {
        this.thingsToBring = thingsToBring;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(final String hashtag) {
        this.hashtag = hashtag;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getPicasaTag() {
        return picasaTag;
    }

    public void setPicasaTag(final String picasaTag) {
        this.picasaTag = picasaTag;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(final Location location) {
        this.location = location;
    }

    public ScheduleDay getScheduleDay() {
        return scheduleDay;
    }

    public void setScheduleDay(final ScheduleDay scheduleDay) {
        this.scheduleDay = scheduleDay;
    }

    public Set<EventRole> getRoles() {
        return roles;
    }

    public void setRoles(final Set<EventRole> roles) {
        this.roles = roles;
    }

    /**
     * @return generate comma separated text with {@link EventRole}s
     */
    @Transient
    public String getRolesPrint() {
        StringBuilder sb = new StringBuilder();
        for (EventRole role : roles) {
            sb.append(role.getName()).append(", ");
        }
        if (sb.length() == 0) {
            return null;
        }
        return sb.substring(0, sb.lastIndexOf(","));
    }

    @Transient
    public String getFullPicasaTag() {
        return "event$" + picasaTag;
    }
}
