package com.myicpc.model.teamInfo;

import com.myicpc.enums.ContestParticipantRole;
import com.myicpc.model.IdGeneratedContestObject;
import com.myicpc.model.contest.Contest;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 * Association between {@link TeamInfo} and {@link ContestParticipant} which holds
 * information about {@link com.myicpc.enums.ContestParticipantRole}
 *
 * @author Roman Smetana
 */
@Entity
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "ContestParticipantAssociation_id_seq")
public class ContestParticipantAssociation extends IdGeneratedContestObject {
    private static final long serialVersionUID = 6878322481787347807L;

    @Enumerated(EnumType.STRING)
    private ContestParticipantRole contestParticipantRole;

    @ManyToOne
    @JoinColumn(name = "teamInfoId", referencedColumnName = "id")
    private TeamInfo teamInfo;

    @NotNull
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
    public boolean isStaffMember(Contest contest) {
        return hasRole(ContestParticipantRole.STAFF, contest);
    }

    @Transient
    public boolean isContestant(Contest contest) {
        return hasRole(ContestParticipantRole.CONTESTANT, contest);
    }

    @Transient
    public boolean isContestParticipant(Contest contest) {
        return hasRole(ContestParticipantRole.CONTESTANT, contest) || hasRole(ContestParticipantRole.ATTENDEE, contest) || hasRole(ContestParticipantRole.COACH, contest) || hasRole(ContestParticipantRole.RESERVE, contest);
    }

    @Transient
    private boolean hasRole(final ContestParticipantRole contestParticipantRole, Contest contest) {
        return getContestParticipantRole() == contestParticipantRole && this.contest.getId().equals(contest.getId());
    }
}
