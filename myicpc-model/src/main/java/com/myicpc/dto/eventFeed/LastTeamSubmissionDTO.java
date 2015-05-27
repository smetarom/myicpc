package com.myicpc.dto.eventFeed;

import java.io.Serializable;

/**
 * @author Roman Smetana
 */
public class LastTeamSubmissionDTO implements Serializable {
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

    public LastTeamSubmissionDTO(long contestId, long teamId, long problemId, long submissionId, boolean solved, boolean penalty, Integer attempts, Double time, boolean judged, boolean firstSolved) {
        this.solved = solved;
        this.penalty = penalty;
        this.attempts = attempts;
        this.time = time;
        this.judged = judged;
        this.firstSolved = firstSolved;
        this.teamId = teamId;
        this.problemId = problemId;
        this.submissionId = submissionId;
        this.contestId = contestId;
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
