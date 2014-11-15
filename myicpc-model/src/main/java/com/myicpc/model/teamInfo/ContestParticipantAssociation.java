package com.myicpc.model.teamInfo;

import com.myicpc.enums.ContestParticipantRole;
import com.myicpc.model.IdGeneratedObject;

import javax.persistence.*;

/**
 * Association between {@link TeamInfo} and {@link ContestParticipant} which holds
 * information about {@link com.myicpc.enums.ContestParticipantRole}
 *
 * @author Roman Smetana
 */
@Entity
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "ContestParticipantAssociation_id_seq")
public class ContestParticipantAssociation extends IdGeneratedObject {
    private static final long serialVersionUID = 6878322481787347807L;

    @Enumerated(EnumType.STRING)
    private ContestParticipantRole contestParticipantRole;

    @ManyToOne
    @JoinColumn(name = "teamInfoId", referencedColumnName = "id")
    private TeamInfo teamInfo;

    @ManyToOne
    @JoinColumn(name = "contestParticipantId", referencedColumnName = "id")
    private ContestParticipant contestParticipant;

    public ContestParticipantRole getContestParticipantRole() {
        return contestParticipantRole;
    }

    public void setContestParticipantRole(final ContestParticipantRole contestParticipantRole) {
        this.contestParticipantRole = contestParticipantRole;
    }

    public TeamInfo getTeamInfo() {
        return teamInfo;
    }

    public void setTeamInfo(final TeamInfo teamInfo) {
        this.teamInfo = teamInfo;
    }

    public ContestParticipant getContestParticipant() {
        return contestParticipant;
    }

    public void setContestParticipant(final ContestParticipant contestParticipant) {
        this.contestParticipant = contestParticipant;
    }

    @Transient
    public boolean isStaffMember() {
        return hasRole(ContestParticipantRole.STAFF);
    }

    @Transient
    public boolean isContestant() {
        return hasRole(ContestParticipantRole.CONTESTANT);
    }

    @Transient
    public boolean isContestParticipant() {
        return hasRole(ContestParticipantRole.CONTESTANT) || hasRole(ContestParticipantRole.ATTENDEE) || hasRole(ContestParticipantRole.COACH) || hasRole(ContestParticipantRole.RESERVE);
    }

    @Transient
    private boolean hasRole(final ContestParticipantRole contestParticipantRole) {
        return getContestParticipantRole() == contestParticipantRole;
    }
}
