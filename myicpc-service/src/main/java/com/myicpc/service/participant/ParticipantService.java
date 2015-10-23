package com.myicpc.service.participant;

import com.myicpc.enums.ContestParticipantRole;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.teamInfo.ContestParticipant;
import com.myicpc.model.teamInfo.ContestParticipantAssociation;
import com.myicpc.model.teamInfo.TeamInfo;
import com.myicpc.repository.teamInfo.ContestParticipantAssociationRepository;
import com.myicpc.repository.teamInfo.ContestParticipantRepository;
import com.myicpc.repository.teamInfo.TeamInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.List;

/**
 * Service responsible for {@link ContestParticipant}s
 *
 * @author Roman Smetana
 */
@Service
@Transactional
public class ParticipantService {
    @Autowired
    private TeamInfoRepository teamInfoRepository;

    @Autowired
    private ContestParticipantRepository contestParticipantRepository;

    @Autowired
    private ContestParticipantAssociationRepository contestParticipantAssociationRepository;

    /**
     * Creates a contest participant
     *
     * @param contestParticipant
     *            contest participant
     * @param participantRole
     *            contest participant role
     * @param teamInfoId
     *            team of the contest participant
     */
    public void createContestParticipant(final ContestParticipant contestParticipant, final Contest contest, final String participantRole, final Long teamInfoId) {
        if (contestParticipant == null || participantRole == null || teamInfoId == null && !"Staff".equalsIgnoreCase(participantRole)) {
            throw new ValidationException("Not all required fields filled in.");
        }
        // set role to new participant
        ContestParticipantRole contestParticipantRole = null;
        try {
            contestParticipantRole = ContestParticipantRole.valueOf(participantRole);
        } catch (IllegalArgumentException | NullPointerException ex) {
            throw new ValidationException("Unknown contest participant role.", ex);
        }
        // associate with team if not staff member
        TeamInfo teamInfo = null;
        if (ContestParticipantRole.STAFF != contestParticipantRole) {
            teamInfo = teamInfoRepository.findOne(teamInfoId);
        }
        ContestParticipant persistedcContestParticipant = contestParticipantRepository.save(contestParticipant);

        ContestParticipantAssociation association = new ContestParticipantAssociation();
        association.setContestParticipant(persistedcContestParticipant);
        association.setContestParticipantRole(contestParticipantRole);
        association.setTeamInfo(teamInfo);
        association.setContest(contest);
        contestParticipantAssociationRepository.save(association);
    }

    /**
     * Returns sorted teams by team/university names based on {@link Contest#showTeamNames}
     *
     * @param contest contest
     * @return sorted teams
     */
    public List<TeamInfo> getTeamInfosSortedByName(Contest contest) {
        if (contest.isShowTeamNames()) {
            return teamInfoRepository.findByContestOrderByNameAsc(contest);
        } else {
            return teamInfoRepository.findByContestOrderByUniversityNameAsc(contest);
        }
    }
}
