package com.myicpc.model.eventFeed;

import com.myicpc.model.IdGeneratedContestObject;
import com.myicpc.model.teamInfo.TeamInfo;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * A team from the event feed
 * <p/>
 * All data for this entity is received from XML feed, which is available after
 * the contest start. For this reason, there is another entity
 * {@linkplain com.myicpc.model.teamInfo.TeamInfo} which is created through synchronization with CM4
 *
 * @author Roman Smetana
 * @see com.myicpc.model.teamInfo.TeamInfo
 */
@Cacheable
@Entity
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "Team_id_seq")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"systemId", "contestId"})})
public class Team extends IdGeneratedContestObject {
    private static final long serialVersionUID = -2333804505185439684L;

    private Long systemId;
    /**
     * Reservation id from CM
     */
    @NotNull
    @Column(unique = true)
    private Long externalId;
    /**
     * Current team rank in the contest
     */
    private Integer rank;
    /**
     * Team name, given by XML feed
     */
    private String name;
    /**
     * Country of the team
     * <p/>
     * 3 letters abbreviation
     */
    private String nationality;
    /**
     * Number of problems solved by team
     */
    private Integer problemsSolved;
    /**
     * Total number of time needed to solve {@link #problemsSolved}
     */
    private Integer totalTime;

    @OneToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "externalIdLinToTeamInfo", referencedColumnName = "externalId")
    private TeamInfo teamInfo;

    /**
     * List of all team submissions for each problem
     */
    @OneToMany(mappedBy = "team")
    private List<TeamProblem> teamProblems;

    /**
     * List of all team submissions for each problem
     */
    @OneToMany(mappedBy = "team")
    private List<LastTeamProblem> lastTeamProblems;

    @Transient
    private boolean followed;

    /**
     * List of most recent team submissions for each problem
     */
    @Transient
    private Map<Long, TeamProblem> currentProblems;

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public Long getExternalId() {
        return externalId;
    }

    public void setExternalId(final Long externalId) {
        this.externalId = externalId;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(final Integer rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(final String nationality) {
        this.nationality = nationality;
    }

    public Integer getProblemsSolved() {
        return problemsSolved;
    }

    public void setProblemsSolved(final Integer problemsSolved) {
        this.problemsSolved = problemsSolved;
    }

    public Integer getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(final Integer totalTime) {
        this.totalTime = totalTime;
    }

    public TeamInfo getTeamInfo() {
        return teamInfo;
    }

    public void setTeamInfo(TeamInfo teamInfo) {
        this.teamInfo = teamInfo;
    }

    public List<TeamProblem> getTeamProblems() {
        return teamProblems;
    }

    public void setTeamProblems(final List<TeamProblem> teamProblems) {
        this.teamProblems = teamProblems;
    }

    public Map<Long, TeamProblem> getCurrentProblems() {
        return currentProblems;
    }

    public void setCurrentProblems(final Map<Long, TeamProblem> currentProblems) {
        this.currentProblems = currentProblems;
    }

    public List<LastTeamProblem> getLastTeamProblems() {
        return lastTeamProblems;
    }

    public void setLastTeamProblems(final List<LastTeamProblem> lastTeamProblems) {
        this.lastTeamProblems = lastTeamProblems;
    }

    public boolean isFollowed() {
        return followed;
    }

    public void setFollowed(final boolean followed) {
        this.followed = followed;
    }
}
