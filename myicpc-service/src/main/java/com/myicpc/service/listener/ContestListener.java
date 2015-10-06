package com.myicpc.service.listener;

import com.myicpc.model.contest.Contest;

/**
 * Defines various events in the contest lifecycle
 *
 * These events are triggered in other services
 *
 * @author Roman Smetana
 */
public interface ContestListener {
    /**
     * Event triggered when a new contest is created
     *
     * @param contest new created contest
     */
    void onContestCreated(Contest contest);
}
