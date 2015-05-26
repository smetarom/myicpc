package com.myicpc.model.eventFeed;

import com.myicpc.model.IdGeneratedObject;
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
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"teamId", "problemId"})})
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "LastTeamSubmission_id_seq")
public class LastTeamSubmission extends IdGeneratedObject {
    /**
     * Is this submission, which solves the given problem
     */
    private boolean solved;
    /**
     * Is team penalized for this submission
     */
    private boolean penalty;
    /**
     * Number of unsuccessful attempts, which preceded to this submission, plus
     * this submission
     */
    private Integer attempts;
    /**
     * Time, when the submission was submitted
     */
    private Double time;
    /**
     * Is this submission judged with judgment system
     */
    private boolean judged;
    /**
     * Is this the first successful submission of the problem in the contest
     */
    private boolean firstSolved;
    private long teamId;
    private long problemId;
    private long submissionId;
    private long contestId;

    public LastTeamSubmission() {
    }

    public LastTeamSubmission(final TeamProblem submission) {
        this.contestId = submission.getTeam().getContest().getId();
        this.problemId = submission.getProblem().getId();
        this.teamId = submission.getTeam().getId();
        update(submission);
    }

    public LastTeamSubmission(final TeamProblem submission, long contestId, long problemId, long teamId) {
        this.contestId = contestId;
        this.problemId = problemId;
        this.teamId = teamId;

        this.attempts = submission.getAttempts();
        this.judged = submission.getJudged();
        this.solved = submission.getSolved();
        this.penalty = submission.getPenalty();
        this.time = submission.getTime();
        this.firstSolved = submission.isFirstSolved();
    }

    @Transient
    public void update(final TeamProblem submission) {
        this.submissionId = submission.getId();

        this.attempts = submission.getAttempts();
        this.judged = submission.getJudged();
        this.solved = submission.getSolved();
        this.penalty = submission.getPenalty();
        this.time = submission.getTime();
        this.firstSolved = submission.isFirstSolved();
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public boolean isPenalty() {
        return penalty;
    }

    public void setPenalty(boolean penalty) {
        this.penalty = penalty;
    }

    public Integer getAttempts() {
        return attempts;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }

    public Double getTime() {
        return time;
    }

    public void setTime(Double time) {
        this.time = time;
    }

    public boolean isJudged() {
        return judged;
    }

    public void setJudged(boolean judged) {
        this.judged = judged;
    }

    public boolean isFirstSolved() {
        return firstSolved;
    }

    public void setFirstSolved(boolean firstSolved) {
        this.firstSolved = firstSolved;
    }

    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    public long getProblemId() {
        return problemId;
    }

    public void setProblemId(long problemId) {
        this.problemId = problemId;
    }

    public long getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(long submissionId) {
        this.submissionId = submissionId;
    }

    public long getContestId() {
        return contestId;
    }

    public void setContestId(long contestId) {
        this.contestId = contestId;
    }
}
