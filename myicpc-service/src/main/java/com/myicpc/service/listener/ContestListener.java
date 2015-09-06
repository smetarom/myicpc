package com.myicpc.service.listener;

import com.myicpc.model.contest.Contest;

/**
 * @author Roman Smetana
 */
public interface ContestListener {
    void onContestCreated(Contest contest);
}
