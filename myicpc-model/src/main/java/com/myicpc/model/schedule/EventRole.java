package com.myicpc.model.schedule;

import com.myicpc.model.IdGeneratedContestObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Represents a role (e.g., contestant) in the schedule
 *
 * @author Roman Smetana
 */
@Entity
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "EventRole_id_seq")
public class EventRole extends IdGeneratedContestObject {
    private static final long serialVersionUID = 8996763498610961609L;

    /**
     * Name of the role
     */
    @NotNull
    @Column(unique = true)
    private String name;

    /**
     * List of events where the role is invited
     */
    @ManyToMany(mappedBy = "roles")
    private List<Event> events;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(final List<Event> events) {
        this.events = events;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (name == null ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        EventRole other = (EventRole) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }
}
