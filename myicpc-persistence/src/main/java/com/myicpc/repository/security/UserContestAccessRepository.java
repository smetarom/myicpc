package com.myicpc.repository.security;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.security.SystemUser;
import com.myicpc.model.security.UserContestAccess;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserContestAccessRepository extends JpaRepository<UserContestAccess, Long> {
    UserContestAccess findBySystemUserAndContest(SystemUser systemUser, Contest contest);

    List<UserContestAccess> findByContest(Contest contest);

    List<UserContestAccess> findByContest(Contest contest, Sort sort);

    List<UserContestAccess> findBySystemUser(SystemUser systemUser);
}
