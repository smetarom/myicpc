package com.myicpc.service.contest;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.security.SystemUser;
import com.myicpc.model.security.UserContestAccess;
import com.myicpc.repository.security.UserContestAccessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service handles user access to contests
 *
 * @author Roman Smetana
 */
@Service
public class ContestAccessService {

    @Autowired
    private UserContestAccessRepository userContestAccessRepository;

    /**
     * Get people, who have access to {@code contest}
     *
     * @param contest contest
     * @return contest managers
     */
    public List<UserContestAccess> getContestManagers(final Contest contest) {
        return userContestAccessRepository.findByContest(contest);
    }

    /**
     * Adds an access to {@code contest} to {@code systemUser}
     *
     * @param systemUser user to get contest access
     * @param contest contest
     * @return created contest access
     */
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

    /**
     * Deletes contest access for {@code systemUser}
     *
     * @param systemUser user to be removed from {@code contest}
     * @param contest contest
     */
    public void deleteContestAccess(final SystemUser systemUser, final Contest contest) {
        if (systemUser == null || contest == null) {
            return;
        }
        UserContestAccess contestAccess = userContestAccessRepository.findBySystemUserAndContest(systemUser, contest);
        userContestAccessRepository.delete(contestAccess);
    }

    /**
     * Deletes {@link UserContestAccess} by ID
     *
     * @param contestAccessId contest access ID to be deleted
     */
    public void deleteContestAccess(final Long contestAccessId) {
        if (contestAccessId == null) {
            return;
        }
        userContestAccessRepository.delete(contestAccessId);
    }
}
