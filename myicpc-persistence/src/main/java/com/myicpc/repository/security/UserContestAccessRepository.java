package com.myicpc.repository.security;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.security.SystemUser;
import com.myicpc.model.security.UserContestAccess;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserContestAccessRepository extends JpaRepository<UserContestAccess, Long> {
    UserContestAccess findBySystemUserAndContest(SystemUser systemUser, Contest contest);

    List<UserContestAccess> findByContest(Contest contest);

    List<UserContestAccess> findByContest(Contest contest, Sort sort);

    List<UserContestAccess> findBySystemUser(SystemUser systemUser);

    @Transactional
    @Modifying
    @Query("DELETE FROM UserContestAccess uca WHERE uca.contest = ?1")
    void deleteByContest(Contest contest);
}
