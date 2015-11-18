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
import com.myicpc.service.listener.ContestListener;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service responsible for {@link Contest} management
 *
 * @author Roman Smetana
 */
@Service
public class ContestService {
    private static final Logger logger = LoggerFactory.getLogger(ContestService.class);
    private final Set<ContestListener> scoreboardListeners = Collections.synchronizedSet(new HashSet<ContestListener>());

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private ContestAccessService contestAccessService;

    @Autowired
    private SystemUserRepository systemUserRepository;

    @Autowired
    private UserContestAccessRepository userContestAccessRepository;

    @Autowired(required = false)
    @Qualifier("contestSocialService")
    private ContestListener contestSocialService;

    /**
     * Adds contest event listeners
     *
     * @see ContestListener
     */
    @PostConstruct
    private void init() {
        if (contestSocialService != null) {
            scoreboardListeners.add(contestSocialService);
        }
    }

    /**
     * Returns all active contests at the moment
     *
     * @return active contests
     */
    public List<Contest> getActiveContests() {
        Sort sort = new Sort(Sort.Direction.DESC, "startTime");
        Date date = new Date();
        date = DateUtils.addDays(date, -7);
        return contestRepository.findByStartTimeGreaterThanEqual(date, sort);
    }

    /**
     * Returns all active contests at the moment, where the logged user has access
     *
     * @return logged user active contests
     */
    @PostFilter(SecurityConstants.FILTER_CONTEST_READ_ACCESS_OR_ADMIN)
    public List<Contest> getActiveContestsSecured() {
        return getActiveContests();
    }

    /**
     * Returns sorted contests by {@code sort}, where the logged user has access
     *
     * @param sort contest sort preferences
     * @return logged user sorted contests
     */
    @PostFilter(SecurityConstants.FILTER_CONTEST_READ_ACCESS_OR_ADMIN)
    public List<Contest> getContestsSecured(Sort sort) {
        return contestRepository.findAll(sort);
    }

    /**
     * Returns all visible contests for public pages
     *
     * @return visible contests
     */
    public List<Contest> getVisibleContests() {
        Sort sort = new Sort(Sort.Direction.DESC, "startTime");
        return contestRepository.findByHidden(false, sort);
    }

    /**
     * Returns a {@link Contest} by {@link Contest#code}
     *
     * The returned value is cached for performance reasons
     *
     * @param contestCode contest code
     * @return contest
     * @throws ContestNotFoundException contest with {@code contestCode} not found
     */
    @Cacheable(value = "contestByCode")
    public Contest getContest(String contestCode) throws ContestNotFoundException {
        Contest contest = contestRepository.findFullByCode(contestCode);
        if (contest == null) {
            throw new ContestNotFoundException("Contest with code " + contestCode + " not found.", contestCode);
        }
        return contest;
    }

    /**
     * Returns a {@link Contest} by {@link Contest#code} if the logged user hac contest access
     *
     * @param contestCode contest code
     * @return contest
     * @throws ContestNotFoundException contest with {@code contestCode} not found
     */
    @PostAuthorize(SecurityConstants.RETURN_CONTEST_READ_ACCESS_OR_ADMIN)
    @Cacheable(value = "contestByCode")
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

    /**
     * Creates a new {@link Contest}
     *
     * It adds current logged user, who created the contest as contest manager
     *
     * It calls {@link ContestListener#onContestCreated(Contest)} on all registered listeners
     *
     * @param contest contest to be created
     */
    @Transactional
    public void createContest(final Contest contest) {
        Contest persistedContest = saveContest(contest);

        // add contest access to logged user
        LoggedUser loggedUser = (LoggedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (loggedUser != null) {
            SystemUser systemUser = systemUserRepository.findByUsername(loggedUser.getUsername());
            UserContestAccess contestAccess = contestAccessService.addContestAccess(systemUser, persistedContest);
            if (contestAccess != null) {
                loggedUser.addContest(contest);
            }
        }

        // call contest created callbacks
        for (ContestListener scoreboardListener : scoreboardListeners) {
            try {
                scoreboardListener.onContestCreated(persistedContest);
            } catch (Throwable e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    /**
     * Persists changes in the contest
     *
     * It does some small auto corrections in the contest
     *
     * @param contest contest to be persisted
     * @return persisted contest
     */
    @Transactional
    public Contest saveContest(final Contest contest) {
        // remove # from hashtag
        contest.setHashtag(FormatUtils.removeHashFromHashtag(contest.getHashtag()));
        return contestRepository.save(contest);
    }

    /**
     * Deletes permanently {@code contest}
     *
     * It deletes all dependent entities to the contest as well
     *
     * @param contest contest to be deleted
     */
    @Transactional
    public void deleteContest(final Contest contest) {
        // TODO remove all objects related to the contest
        userContestAccessRepository.deleteByContest(contest);

        contestRepository.delete(contest);
    }
}
