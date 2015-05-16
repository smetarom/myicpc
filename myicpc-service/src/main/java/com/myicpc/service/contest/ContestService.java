package com.myicpc.service.contest;

import com.myicpc.model.contest.Contest;
import com.myicpc.repository.contest.ContestRepository;
import com.myicpc.service.exception.ContestNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author Roman Smetana
 */
@Service
@Transactional
public class ContestService {
    @Autowired
    private ContestRepository contestRepository;

    public List<Contest> getActiveContests() {
        Sort sort = new Sort(Sort.Direction.DESC, "startTime");
        return contestRepository.findAll(sort);
    }

    public Contest getContest(String contestCode) throws ContestNotFoundException {
        Contest contest = contestRepository.findByCode(contestCode);
        if (contest == null) {
            throw new ContestNotFoundException("Contest with code " + contestCode + " not found.");
        }
        return contest;
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

    public void saveContest(Contest contest) {
        contestRepository.save(contest);
    }

    public void deleteContest(Contest contest) {
        contestRepository.delete(contest);
    }

}
