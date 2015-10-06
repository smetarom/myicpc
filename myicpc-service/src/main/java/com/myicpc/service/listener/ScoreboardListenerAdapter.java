package com.myicpc.service.listener;

import com.myicpc.model.eventFeed.Team;
import com.myicpc.model.eventFeed.TeamProblem;

import java.util.List;

/**
 * Adapter for {@link ScoreboardListener}
 *
 * It prevents from the implementing all methods in the {@link ScoreboardListener} implementation,
 * the implementation overrides only methods it uses
 *
 * @author Roman Smetana
 */
public abstract class ScoreboardListenerAdapter implements ScoreboardListener {
    @Override
    public void onSubmission(TeamProblem teamProblem, List<Team> effectedTeams) {

    }

    @Override
    public void onFailedSubmissionNotification(TeamProblem teamProblem) {

    }

    @Override
    public void onSuccessSubmissionNotification(TeamProblem teamProblem) {

    }

    @Override
    public void onPendingSubmissionNotification(TeamProblem teamProblem) {

    }
}
