package com.myicpc.model.eventFeed;

import com.myicpc.commons.utils.TextUtils;
import com.myicpc.model.IdGeneratedObject;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Represents one team submission of the problem solution
 * <p/>
 * Contains all information from Event feed about <em>run</em>, which are
 * related to MyICPC
 *
 * @author Roman Smetana
 * @see Problem
 * @see Team
 */
@Entity
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "TeamProblem_id_seq")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"systemId", "teamId", "problemId"})})
public class TeamProblem extends IdGeneratedObject {
    private static final long serialVersionUID = -6311808642845144604L;

    private Long systemId;
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
     * UNIX timestamp, when the submission was submitted
     */
    private Date timestamp;
    /**
     * Is this submission judged with judgment system
     */
    private boolean judged;
    /**
     * Is this the first successful submission of the problem in the contest
     */
    private boolean firstSolved;
    /**
     * {@link Team#getRank()} before submission
     */
    private int oldRank;
    /**
     * {@link Team#getRank()} after submission
     */
    private int newRank;
    /**
     * How many testcases the submission passed in judgment system
     */
    private int numTestPassed;
    private int totalNumTests;
    /**
     * Programming language used for this submission
     */
    private String language;
    /**
     * Final judgment for this submission
     */
    private String resultCode;
    /**
     * Helper field, to save submission status during the feed parsing
     */
    @Transient
    private String status;

    /**
     * Team, which submitted
     */
    @ManyToOne
    @JoinColumn(name = "teamId")
    private Team team;
    /**
     * Problem, which the submission tries to solve
     */
    @ManyToOne
    @JoinColumn(name = "problemId")
    private Problem problem;

    @OneToOne(fetch=FetchType.LAZY, mappedBy="teamProblem", cascade = CascadeType.ALL, optional = true)
    private LastTeamProblem lastTeamProblem;

    public Team getTeam() {
        return team;
    }

    public void setTeam(final Team team) {
        this.team = team;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(final Problem problem) {
        this.problem = problem;
    }

    public LastTeamProblem getLastTeamProblem() {
        return lastTeamProblem;
    }

    public void setLastTeamProblem(LastTeamProblem lastTeamProblem) {
        this.lastTeamProblem = lastTeamProblem;
    }

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public boolean getSolved() {
        return solved;
    }

    public void setSolved(final boolean solved) {
        this.solved = solved;
    }

    public boolean getPenalty() {
        return penalty;
    }

    public void setPenalty(final boolean penalty) {
        this.penalty = penalty;
    }

    public Integer getAttempts() {
        return attempts;
    }

    public void setAttempts(final Integer attempts) {
        this.attempts = attempts;
    }

    public Double getTime() {
        return time;
    }

    public void setTime(final Double time) {
        this.time = time;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final Date timestamp) {
        this.timestamp = timestamp;
    }

    public boolean getJudged() {
        return judged;
    }

    public void setJudged(final boolean status) {
        this.judged = status;
    }

    public boolean isFirstSolved() {
        return firstSolved;
    }

    public void setFirstSolved(final boolean firstSolved) {
        this.firstSolved = firstSolved;
    }

    public int getNumTestPassed() {
        return numTestPassed;
    }

    public void setNumTestPassed(final int numTestPassed) {
        this.numTestPassed = numTestPassed;
    }

    public int getTotalNumTests() {
        return totalNumTests;
    }

    public void setTotalNumTests(int totalNumTests) {
        this.totalNumTests = totalNumTests;
    }

    /**
     * @return human readable time in hours and minutes
     * @see com.myicpc.commons.utils.TextUtils#formatTimeToHoursMinutes(Double)
     */
    @Transient
    public String getFormattedTime() {
        return TextUtils.formatTimeToHoursMinutes(time);
    }

    /**
     * @return human readable time in minutes
     * @see com.myicpc.commons.utils.TextUtils#formatTimeToMinutes(Double)
     */
    @Transient
    public String getTimeInMinutes() {
        return TextUtils.formatTimeToMinutes(time);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public int getOldRank() {
        return oldRank;
    }

    public void setOldRank(final int oldRank) {
        this.oldRank = oldRank;
    }

    public int getNewRank() {
        return newRank;
    }

    public void setNewRank(final int newRank) {
        this.newRank = newRank;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(final String language) {
        this.language = language;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(final String resultCode) {
        this.resultCode = resultCode;
    }
}
