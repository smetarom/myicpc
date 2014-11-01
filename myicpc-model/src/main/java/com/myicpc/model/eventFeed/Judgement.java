package com.myicpc.model.eventFeed;

import com.myicpc.model.IdGeneratedObject;
import com.myicpc.model.contest.Contest;

import javax.persistence.*;

/**
 * Judgment of the submission, which determine if the problem is solved or not
 *
 * @author Roman Smetana
 */
@Entity
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "Judgement_id_seq")
public class Judgement extends IdGeneratedObject {
    private static final long serialVersionUID = 3487278240444236481L;
    /**
     * Short name
     */
    @Column(unique = true)
    private String code;
    /**
     * Descriptive name
     */
    private String name;
    /**
     * Color, which represents {@link Judgement} in the graphical representation
     */
    private String color;

    @ManyToOne
    @JoinColumn(name = "contestId")
    private Contest contest;

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

    public Contest getContest() {
        return contest;
    }

    public void setContest(Contest contest) {
        this.contest = contest;
    }
}
