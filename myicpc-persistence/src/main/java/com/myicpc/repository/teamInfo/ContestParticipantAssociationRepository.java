package com.myicpc.repository.teamInfo;

import com.myicpc.model.teamInfo.TeamInfo;
import com.myicpc.model.teamInfo.ContestParticipant;
import com.myicpc.model.teamInfo.ContestParticipantAssociation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

public interface ContestParticipantAssociationRepository extends CrudRepository<ContestParticipantAssociation, Long> {
    ContestParticipantAssociation findByTeamInfoAndContestParticipant(TeamInfo teamInfo, ContestParticipant contestParticipant);

    List<ContestParticipantAssociation> findByContestParticipant(ContestParticipant contestParticipant);

    @Query(value = "FROM ContestParticipantAssociation cpa JOIN FETCH cpa.contestParticipant c WHERE c IN ?1")
    List<ContestParticipantAssociation> findByContestParticipantIn(Collection<ContestParticipant> contestParticipant);
}
