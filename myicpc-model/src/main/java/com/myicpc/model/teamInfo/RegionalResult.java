package com.myicpc.model.teamInfo;

import com.myicpc.model.IdGeneratedObject;

import javax.persistence.*;

/**
 * Results in the regionals from registration system
 *
 * @author Roman Smetana
 */
@Entity
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "RegionResult_id_seq")
public class RegionalResult extends IdGeneratedObject {
    private static final long serialVersionUID = -5465875598534133852L;

    /**
     * Contest id from registration system
     */
    private Long contestId;
    /**
     * Contest name
     */
    private String contestName;
    /**
     * Name of the team from regional contest
     */
    private String teamName;
    /**
     * Regional rank
     */
    private int rank;
    /**
     * Number of solved problems by the team
     */
    private int problemSolved;
    /**
     * Total time score in minutes
     */
    private int totalTime;
    /**
     * Last problem solved time in minutes
     */
    private int lastProblemSolved;

    /**
     * Team, which compete in MyICPC contest
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamInfoId")
    private TeamInfo teamInfo;

    public Long getContestId() {
        return contestId;
    }

    public void setContestId(final Long contestId) {
        this.contestId = contestId;
    }

    public String getContestName() {
        return contestName;
    }

    public void setContestName(final String contestName) {
        this.contestName = contestName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(final String teamName) {
        this.teamName = teamName;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(final int rank) {
        this.rank = rank;
    }

    public int getProblemSolved() {
        return problemSolved;
    }

    public void setProblemSolved(final int problemSoved) {
        this.problemSolved = problemSoved;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(final int totalTime) {
        this.totalTime = totalTime;
    }

    public int getLastProblemSolved() {
        return lastProblemSolved;
    }

    public void setLastProblemSolved(final int lastProblemSolved) {
        this.lastProblemSolved = lastProblemSolved;
    }

    public TeamInfo getTeamInfo() {
        return teamInfo;
    }

    public void setTeamInfo(final TeamInfo teamInfo) {
        this.teamInfo = teamInfo;
    }
}
