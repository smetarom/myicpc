package com.myicpc.dto.eventFeed;

import java.io.Serializable;

/**
 * @author Roman Smetana
 */
public class TeamSubmissionDTO implements Serializable {
    private final Long teamSubmissionId;
    private final Long teamId;
    private final String teamName;
    private final boolean solved;
    private final boolean penalty;
    private final Double time;
    private final boolean judged;
    private final int numTestPassed;
    private final int totalNumTests;
    private final String language;
    private final String judgement;

    public TeamSubmissionDTO(Long teamSubmissionId, Long teamId, String teamName, boolean solved, boolean penalty, Double time, boolean judged, int numTestPassed, int totalNumTests, String language, String judgement) {
        this.teamSubmissionId = teamSubmissionId;
        this.teamId = teamId;
        this.teamName = teamName;
        this.solved = solved;
        this.penalty = penalty;
        this.time = time;
        this.judged = judged;
        this.numTestPassed = numTestPassed;
        this.totalNumTests = totalNumTests;
        this.language = language;
        this.judgement = judgement;
    }

    public Long getTeamSubmissionId() {
        return teamSubmissionId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public boolean isSolved() {
        return solved;
    }

    public boolean isPenalty() {
        return penalty;
    }

    public Double getTime() {
        return time;
    }

    public boolean isJudged() {
        return judged;
    }

    public int getNumTestPassed() {
        return numTestPassed;
    }

    public int getTotalNumTests() {
        return totalNumTests;
    }

    public String getLanguage() {
        return language;
    }

    public String getJudgement() {
        return judgement;
    }
}
