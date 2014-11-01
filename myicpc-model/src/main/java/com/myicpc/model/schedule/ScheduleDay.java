package com.myicpc.model.schedule;

import com.myicpc.commons.utils.TimeUtils;
import com.myicpc.model.IdGeneratedContestObject;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Represents a day in the schedule
 *
 * @author Roman Smetana
 */
@ValidateScheduleDay
@Entity
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "ScheduleDay_id_seq")
public class ScheduleDay extends IdGeneratedContestObject implements Comparable<ScheduleDay> {
    private static final long serialVersionUID = -8937265473589617602L;
    /**
     * Day date
     */
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
    /**
     * Name of the day
     */
    @NotNull
    private String name;
    /**
     * Detailed description of the day
     */
    @Lob
    @Type(type = "org.hibernate.type.StringClobType")
    private String description;
    /**
     * Order of the day within the schedule
     */
    @Column(unique = true)
    private int dayOrder;
    /**
     * List of events in the day
     */
    @OneToMany(mappedBy = "scheduleDay")
    private List<Event> events;

    /**
     * Events sorted by start date
     */
    @Transient
    private List<Event> eventsChronologically;

    public Date getDate() {
        return date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    /**
     * @return date in local time
     */
    @Transient
    public Date getLocalDate() {
        return TimeUtils.convertUTCDateToLocal(getDate(), contest.getContestSettings().getTimeDifference());
    }

    /**
     * @param date date in local time
     */
    @Transient
    public void setLocalDate(final Date date) {
        setDate(TimeUtils.convertLocalDateToUTC(date, contest.getContestSettings().getTimeDifference()));
    }

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

    public int getDayOrder() {
        return dayOrder;
    }

    public void setDayOrder(final int dayOrder) {
        this.dayOrder = dayOrder;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(final List<Event> events) {
        this.events = events;
    }

    public List<Event> getEventsChronologically() {
        return eventsChronologically;
    }

    public void setEventsChronologically(final List<Event> eventsChronologically) {
        this.eventsChronologically = eventsChronologically;
        Collections.sort(this.eventsChronologically, new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                return o1.getStartDate().compareTo(o2.getStartDate());
            }
        });
    }

    @Override
    public int compareTo(ScheduleDay o) {
        return getDate().compareTo(o.getDate());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (date == null ? 0 : date.hashCode());
        result = prime * result + dayOrder;
        result = prime * result + (description == null ? 0 : description.hashCode());
        result = prime * result + (events == null ? 0 : events.hashCode());
        result = prime * result + (eventsChronologically == null ? 0 : eventsChronologically.hashCode());
        result = prime * result + (name == null ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ScheduleDay other = (ScheduleDay) obj;
        if (date == null) {
            if (other.date != null) {
                return false;
            }
        } else if (!date.equals(other.date)) {
            return false;
        }
        if (dayOrder != other.dayOrder) {
            return false;
        }
        if (description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!description.equals(other.description)) {
            return false;
        }
        if (events == null) {
            if (other.events != null) {
                return false;
            }
        } else if (!events.equals(other.events)) {
            return false;
        }
        if (eventsChronologically == null) {
            if (other.eventsChronologically != null) {
                return false;
            }
        } else if (!eventsChronologically.equals(other.eventsChronologically)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getDayOrder() + " - " + getName();
    }
}
