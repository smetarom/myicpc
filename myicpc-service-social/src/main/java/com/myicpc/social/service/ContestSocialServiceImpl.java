package com.myicpc.social.service;

import com.myicpc.model.contest.Contest;
import com.myicpc.service.listener.ContestListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link ContestSocialService}
 *
 * @author Roman Smetana
 */
@Service("contestSocialService")
public class ContestSocialServiceImpl extends ContestListenerAdapter implements ContestSocialService {
    @Autowired
    private TwitterService twitterService;

    @Override
    public void onContestCreated(Contest contest) {
        twitterService.startTwitterStreaming(contest);
    }
}
