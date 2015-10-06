package com.myicpc.service.listener;

import com.myicpc.model.contest.Contest;

/**
 * Adapter for {@link ContestListener}
 *
 * It prevents from the implementing all methods in the {@link ContestListener} implementation,
 * the implementation overrides only methods it uses
 *
 * @author Roman Smetana
 */
public class ContestListenerAdapter implements ContestListener {
    @Override
    public void onContestCreated(Contest contest) {

    }
}
