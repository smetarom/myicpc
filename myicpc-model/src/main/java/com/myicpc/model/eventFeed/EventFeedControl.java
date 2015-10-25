package com.myicpc.model.eventFeed;

import com.myicpc.model.IdGeneratedContestObject;
import com.myicpc.model.contest.Contest;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

/**
 * @author Roman Smetana
 */
@Entity
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "EventFeedControl_id_seq")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"contestId"})})
public class EventFeedControl extends IdGeneratedContestObject {
    private int processedRunsCounter;

    public EventFeedControl() {
    }

    public EventFeedControl(Contest contest) {
        super(contest);
    }

    public int getProcessedRunsCounter() {
        return processedRunsCounter;
    }

    public void setProcessedRunsCounter(int processedRunsCounter) {
        this.processedRunsCounter = processedRunsCounter;
    }

    @Transient
    public void increaseProcessedRunsCounter() {
        processedRunsCounter += 1;
    }
}
