package com.myicpc.model;

import com.myicpc.model.contest.Contest;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

/**
 * Common object to all objects stored in database, which have auto generated
 * primary key
 *
 * @author Roman Smetana
 */
@MappedSuperclass
public abstract class IdGeneratedContestObject extends IdGeneratedObject {
    private static final long serialVersionUID = -348531051405139050L;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "contestId", nullable = false)
    protected Contest contest;

    public IdGeneratedContestObject() {
    }

    public IdGeneratedContestObject(Contest contest) {
        this.contest = contest;
    }

    public Contest getContest() {
        return contest;
    }

    public void setContest(Contest contest) {
        this.contest = contest;
    }
}
