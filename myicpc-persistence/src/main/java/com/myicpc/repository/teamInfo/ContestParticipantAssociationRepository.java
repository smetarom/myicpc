package com.myicpc.repository.teamInfo;

import com.myicpc.enums.ContestParticipantRole;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.teamInfo.ContestParticipant;
import com.myicpc.model.teamInfo.ContestParticipantAssociation;
import com.myicpc.model.teamInfo.TeamInfo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

public interface ContestParticipantAssociationRepository extends CrudRepository<ContestParticipantAssociation, Long> {
    ContestParticipantAssociation findByTeamInfoAndContestParticipant(TeamInfo teamInfo, ContestParticipant contestParticipant);

    List<ContestParticipantAssociation> findByContestParticipant(ContestParticipant contestParticipant);

    List<ContestParticipantAssociation> findByContestParticipantAndContest(ContestParticipant contestParticipant, Contest contest);

    List<ContestParticipantAssociation> findByTeamInfo(TeamInfo teamInfo);

    List<ContestParticipantAssociation> findByContestParticipantRoleAndContest(ContestParticipantRole contestParticipantRole, Contest contest);

    @Query(value = "FROM ContestParticipantAssociation cpa JOIN FETCH cpa.contestParticipant c WHERE c IN ?1")
    List<ContestParticipantAssociation> findByContestParticipantIn(Collection<ContestParticipant> contestParticipant);

    @Query(value = "FROM ContestParticipantAssociation cpa JOIN FETCH cpa.contestParticipant c WHERE c IN ?1 AND cpa.contest = ?2")
    List<ContestParticipantAssociation> findByContestParticipantInAndContest(Collection<ContestParticipant> contestParticipant, Contest contest);

    @Transactional
    @Modifying
    @Query("DELETE FROM ContestParticipantAssociation cpa WHERE cpa.teamInfo = ?1")
    void deleteByTeamInfo(TeamInfo teamInfo);
}
