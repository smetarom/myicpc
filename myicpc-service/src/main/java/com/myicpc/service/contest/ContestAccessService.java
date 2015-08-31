package com.myicpc.service.contest;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.security.SystemUser;
import com.myicpc.model.security.UserContestAccess;
import com.myicpc.repository.security.UserContestAccessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Roman Smetana
 */
@Service
public class ContestAccessService {

    @Autowired
    private UserContestAccessRepository userContestAccessRepository;

    public List<UserContestAccess> getContestManagers(final Contest contest) {
        return userContestAccessRepository.findByContest(contest);
    }


    public UserContestAccess addContestAccess(final SystemUser systemUser, final Contest contest) {
        if (systemUser == null || contest == null) {
            return null;
        }
        UserContestAccess contestAccess = userContestAccessRepository.findBySystemUserAndContest(systemUser, contest);
        if (contestAccess == null) {
            contestAccess = new UserContestAccess();
            contestAccess.setSystemUser(systemUser);
            contestAccess.setContest(contest);
        }
        return userContestAccessRepository.save(contestAccess);
    }

    public void deleteContestAccess(final SystemUser systemUser, final Contest contest) {
        if (systemUser == null || contest == null) {
            return;
        }
        UserContestAccess contestAccess = userContestAccessRepository.findBySystemUserAndContest(systemUser, contest);
        userContestAccessRepository.delete(contestAccess);
    }

    public void deleteContestAccess(final Long contestAccessId) {
        if (contestAccessId == null) {
            return;
        }
        userContestAccessRepository.delete(contestAccessId);
    }
}
