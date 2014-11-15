package com.myicpc.service.contest;

import com.myicpc.model.contest.Contest;
import com.myicpc.repository.contest.ContestRepository;
import com.myicpc.service.exception.ContestNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Roman Smetana
 */
@Service
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

    public void saveContest(Contest contest) {
        contestRepository.save(contest);
    }

    public void deleteContest(Contest contest) {
        contestRepository.delete(contest);
    }

}
