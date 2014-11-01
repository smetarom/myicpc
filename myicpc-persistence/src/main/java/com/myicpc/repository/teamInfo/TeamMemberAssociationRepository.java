package com.myicpc.repository.teamInfo;

import com.myicpc.model.teamInfo.TeamInfo;
import com.myicpc.model.teamInfo.TeamMember;
import com.myicpc.model.teamInfo.TeamMemberAssociation;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TeamMemberAssociationRepository extends CrudRepository<TeamMemberAssociation, Long> {
    TeamMemberAssociation findByTeamInfoAndTeamMember(TeamInfo teamInfo, TeamMember teamMember);

    List<TeamMemberAssociation> findByTeamMember(TeamMember teamMember);
}
