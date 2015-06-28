package com.myicpc.model.eventFeed;

import com.myicpc.model.IdGeneratedContestObject;
import com.myicpc.model.contest.Contest;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Last problem submission for a team
 * <p/>
 * It is only optimization of searching, which submission of the team is the
 * last which counts for given problem
 *
 * @author Roman Smetana
 */
@Cacheable
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"teamId", "problemId", "teamProblemId"}),
        @UniqueConstraint(columnNames = {"teamId", "problemId"})
})
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "LastTeamProblem_id_seq")
public class LastTeamProblem extends IdGeneratedContestObject {
    private static final long serialVersionUID = 2037705931029494546L;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "teamId")
    private Team team;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "problemId")
    private Problem problem;
    @NotNull
    @OneToOne
    @JoinColumn(name = "teamProblemId")
    private TeamProblem teamProblem;

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

    public TeamProblem getTeamProblem() {
        return teamProblem;
    }

    public void setTeamProblem(final TeamProblem teamProblem) {
        this.teamProblem = teamProblem;
    }
}
