package com.myicpc.service.scoreboard.problem;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Problem;
import com.myicpc.repository.eventFeed.ProblemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Roman Smetana
 */
@Service
@Transactional
public class ProblemService {
    @Autowired
    private ProblemRepository problemRepository;

    public List<Problem> findByContest(Contest contest) {
        return problemRepository.findByContestOrderByCodeAsc(contest);
    }
}
