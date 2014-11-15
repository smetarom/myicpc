package com.myicpc.repository.teamInfo;

import com.myicpc.model.teamInfo.TeamInfo;
import com.myicpc.model.teamInfo.ContestParticipant;
import com.myicpc.model.teamInfo.ContestParticipantAssociation;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ContestParticipantAssociationRepository extends CrudRepository<ContestParticipantAssociation, Long> {
    ContestParticipantAssociation findByTeamInfoAndContestParticipant(TeamInfo teamInfo, ContestParticipant contestParticipant);

    List<ContestParticipantAssociation> findByContestParticipant(ContestParticipant contestParticipant);
}
