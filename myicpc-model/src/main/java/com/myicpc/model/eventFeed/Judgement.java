package com.myicpc.model.eventFeed;

import com.myicpc.model.IdGeneratedContestObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Judgment of the submission, which determine if the problem is solved or not
 *
 * @author Roman Smetana
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"code", "contestId"})})
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "Judgement_id_seq")
public class Judgement extends IdGeneratedContestObject {
    private static final long serialVersionUID = 3487278240444236481L;
    /**
     * Short name
     */
    private String code;
    /**
     * Descriptive name
     */
    private String name;
    /**
     * Color, which represents {@link Judgement} in the graphical representation
     */
    private String color;

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(final String color) {
        this.color = color;
    }
}
