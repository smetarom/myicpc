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
    @Transient
    private int runsToSkip;
    @Transient
    private int messagesToSkip;
    @Transient
    private int skippedRuns;
    @Transient
    private int skippedMessages;

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

    public int getRunsToSkip() {
        return runsToSkip;
    }

    public void setRunsToSkip(int runsToSkip) {
        this.runsToSkip = runsToSkip;
    }

    public int getMessagesToSkip() {
        return messagesToSkip;
    }

    public void setMessagesToSkip(int messagesToSkip) {
        this.messagesToSkip = messagesToSkip;
    }

    public int getSkippedRuns() {
        return skippedRuns;
    }

    public void setSkippedRuns(int skippedRuns) {
        this.skippedRuns = skippedRuns;
    }

    public int getSkippedMessages() {
        return skippedMessages;
    }

    public void setSkippedMessages(int skippedMessages) {
        this.skippedMessages = skippedMessages;
    }

    @Transient
    public void restartControl() {
        runsToSkip = processedRunsCounter;
    }

    @Transient
    public void increaseProcessedRunsCounter() {
        processedRunsCounter += 1;
    }

    @Transient
    public void increaseSkippedRuns() {
        skippedRuns += 1;
    }
}
