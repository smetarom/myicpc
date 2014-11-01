package com.myicpc.repository.teamInfo;

import com.myicpc.model.teamInfo.AttendedContest;
import com.myicpc.model.teamInfo.TeamMember;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AttendedContestRepository extends CrudRepository<AttendedContest, Long> {
    AttendedContest findByExternalIdAndTeamMemberExternalId(Long externalId, Long teamMemberExternalId);

    List<AttendedContest> findByTeamMemberOrderByYearDesc(TeamMember teamMember);
}
