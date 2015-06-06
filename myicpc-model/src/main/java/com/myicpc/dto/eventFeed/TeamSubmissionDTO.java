package com.myicpc.dto.eventFeed;

import java.io.Serializable;

/**
 * @author Roman Smetana
 */
public class TeamSubmissionDTO implements Serializable {
    private Long teamSubmissionId;
    private Long teamId;
    private String teamName;
    private boolean solved;
    private boolean penalty;
    private Double time;
    private boolean judged;
    private int numTestPassed;
    private int totalNumTests;

    public TeamSubmissionDTO(Long teamSubmissionId, Long teamId, String teamName, boolean solved, boolean penalty, Double time, boolean judged, int numTestPassed, int totalNumTests) {
        this.teamSubmissionId = teamSubmissionId;
        this.teamId = teamId;
        this.teamName = teamName;
        this.solved = solved;
        this.penalty = penalty;
        this.time = time;
        this.judged = judged;
        this.numTestPassed = numTestPassed;
        this.totalNumTests = totalNumTests;
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
}
