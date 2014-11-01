package com.myicpc.repository.editActivity;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.editActivity.EditActivity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EditActivityRepository extends CrudRepository<EditActivity, Long> {
    EditActivity findByExternalId(Long externalId);

    /**
     * @return maximum number of round for edit activity
     */
    @Query("SELECT MAX(ea.round) FROM EditActivity ea")
    Long findMaxRound();

    List<EditActivity> findByRoundBetween(int min, int max);

    @Transactional
    @Modifying
    @Query("DELETE FROM EditActivity ea WHERE ea.team IN (SELECT t FROM Team t WHERE t.contest = ?1)")
    void deleteByContest(Contest contest);
}
