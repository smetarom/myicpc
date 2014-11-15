package com.myicpc.repository.teamInfo;

import com.myicpc.model.teamInfo.AttendedContest;
import com.myicpc.model.teamInfo.ContestParticipant;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AttendedContestRepository extends CrudRepository<AttendedContest, Long> {
    AttendedContest findByExternalIdAndContestParticipantExternalId(Long externalId, Long contestParticipantExternalId);

    List<AttendedContest> findByContestParticipantOrderByYearDesc(ContestParticipant contestParticipant);
}
