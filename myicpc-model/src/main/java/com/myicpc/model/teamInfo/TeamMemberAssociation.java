package com.myicpc.model.teamInfo;

import com.myicpc.enums.TeamMemberRole;
import com.myicpc.model.IdGeneratedObject;

import javax.persistence.*;

/**
 * Association between {@link TeamInfo} and {@link TeamMember} which holds
 * information about {@link TeamMemberRole}
 *
 * @author Roman Smetana
 */
@Entity
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "TeamMemberAssociation_id_seq")
public class TeamMemberAssociation extends IdGeneratedObject {
    private static final long serialVersionUID = 6878322481787347807L;

    @Enumerated(EnumType.STRING)
    private TeamMemberRole teamMemberRole;

    @ManyToOne
    @JoinColumn(name = "teamInfoId", referencedColumnName = "id")
    private TeamInfo teamInfo;

    @ManyToOne
    @JoinColumn(name = "teamMemberId", referencedColumnName = "id")
    private TeamMember teamMember;

    public TeamMemberRole getTeamMemberRole() {
        return teamMemberRole;
    }

    public void setTeamMemberRole(final TeamMemberRole teamMemberRole) {
        this.teamMemberRole = teamMemberRole;
    }

    public TeamInfo getTeamInfo() {
        return teamInfo;
    }

    public void setTeamInfo(final TeamInfo teamInfo) {
        this.teamInfo = teamInfo;
    }

    public TeamMember getTeamMember() {
        return teamMember;
    }

    public void setTeamMember(final TeamMember teamMember) {
        this.teamMember = teamMember;
    }

    @Transient
    public boolean isStaffMember() {
        return hasRole(TeamMemberRole.STAFF);
    }

    @Transient
    public boolean isContestant() {
        return hasRole(TeamMemberRole.CONTESTANT);
    }

    @Transient
    public boolean isContestParticipant() {
        return hasRole(TeamMemberRole.CONTESTANT) || hasRole(TeamMemberRole.ATTENDEE) || hasRole(TeamMemberRole.COACH) || hasRole(TeamMemberRole.RESERVE);
    }

    @Transient
    private boolean hasRole(final TeamMemberRole teamMemberRole) {
        return getTeamMemberRole() == teamMemberRole;
    }
}
