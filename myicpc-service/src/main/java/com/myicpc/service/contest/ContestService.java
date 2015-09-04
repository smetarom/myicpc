package com.myicpc.service.contest;

import com.myicpc.commons.utils.FormatUtils;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.security.SystemUser;
import com.myicpc.model.security.UserContestAccess;
import com.myicpc.repository.contest.ContestRepository;
import com.myicpc.repository.security.SystemUserRepository;
import com.myicpc.repository.security.UserContestAccessRepository;
import com.myicpc.security.config.SecurityConstants;
import com.myicpc.security.dto.LoggedUser;
import com.myicpc.service.exception.ContestNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author Roman Smetana
 */
@Service
public class ContestService {
    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private ContestAccessService contestAccessService;

    @Autowired
    private SystemUserRepository systemUserRepository;

    @Autowired
    private UserContestAccessRepository userContestAccessRepository;

    public List<Contest> getActiveContests() {
        Sort sort = new Sort(Sort.Direction.DESC, "startTime");
        return contestRepository.findAll(sort);
    }

    @PostFilter(SecurityConstants.FILTER_CONTEST_READ_ACCESS_OR_ADMIN)
    public List<Contest> getActiveContestsSecured() {
        return getActiveContests();
    }

    @PostFilter(SecurityConstants.FILTER_CONTEST_READ_ACCESS_OR_ADMIN)
    public List<Contest> getContestsSecured(Sort sort) {
        return contestRepository.findAll(sort);
    }

    @Cacheable(value = "contestByCode")
    public Contest getContest(String contestCode) throws ContestNotFoundException {
        Contest contest = contestRepository.findFullByCode(contestCode);
        if (contest == null) {
            throw new ContestNotFoundException("Contest with code " + contestCode + " not found.", contestCode);
        }
        return contest;
    }

    @PostAuthorize(SecurityConstants.RETURN_CONTEST_READ_ACCESS_OR_ADMIN)
    public Contest getContestSecured(String contestCode) throws ContestNotFoundException {
        return getContest(contestCode);
    }

    /**
     * Get the current contest time in seconds
     * <p/>
     * The positive number means the contest already started and x seconds elapsed.
     * If the returned number is negative, it means the contest has not yet started
     * and x seconds left to the start of the contest.
     *
     * @param contest contest
     * @return seconds from start of the contest
     */
    public long getCurrentContestTime(Contest contest) {
        Date now = new Date();
        Date contestStartDate = null;
        if (contest != null) {
            contestStartDate = contest.getStartTime();
        }
        long diff = now.getTime() - contestStartDate.getTime();
        diff = diff / 1000;

        if (contest != null && contest.getLength() > 0) {
            diff = Math.min(diff, contest.getLength());
        }

        return diff;
    }

    @Transactional
    public void createContest(final Contest contest) {
        Contest persistedContest = saveContest(contest);
        LoggedUser loggedUser = (LoggedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (loggedUser != null) {
            SystemUser systemUser = systemUserRepository.findByUsername(loggedUser.getUsername());
            UserContestAccess contestAccess = contestAccessService.addContestAccess(systemUser, persistedContest);
            if (contestAccess != null) {
                loggedUser.addContest(contest);
            }
        }
    }

    @Transactional
    public Contest saveContest(final Contest contest) {
        // remove # from hashtag
        contest.setHashtag(FormatUtils.removeHashFromHashtag(contest.getHashtag()));
        return contestRepository.save(contest);
    }

    @Transactional
    public void deleteContest(final Contest contest) {
        // TODO remove all objects related to the contest
        userContestAccessRepository.deleteByContest(contest);

        contestRepository.delete(contest);
    }
}
