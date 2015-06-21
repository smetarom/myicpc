package com.myicpc.dto.insight;

import java.io.Serializable;

/**
 * @author Roman Smetana
 */
public class InsightSubmissionDTO implements Serializable {
    private Long teamId;
    private String teamName;
    private Double time;
    private boolean solved;
    private boolean firstSolved;
    private String language;

    public InsightSubmissionDTO(Long teamId, String teamName, Double time, boolean solved, boolean firstSolved, String language) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.time = time;
        this.solved = solved;
        this.firstSolved = firstSolved;
        this.language = language;
    }

    public Long getTeamId() {
        return teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public Double getTime() {
        return time;
    }

    public boolean isSolved() {
        return solved;
    }

    public boolean isFirstSolved() {
        return firstSolved;
    }

    public String getLanguage() {
        return language;
    }
}
