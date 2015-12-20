package com.myicpc.model.eventFeed;

import com.myicpc.model.IdGeneratedContestObject;
import com.myicpc.model.IdGeneratedObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Programming language for solving the {@link Problem}
 *
 * @author Roman Smetana
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "contestId"})})
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "Language_id_seq")
public class Language extends IdGeneratedContestObject {
    private static final long serialVersionUID = 6551870282448232022L;

    /**
     * Language name
     */
    private String name;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
