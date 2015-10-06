package com.myicpc.service.listener;

import com.myicpc.model.eventFeed.Team;
import com.myicpc.model.eventFeed.TeamProblem;
import com.myicpc.model.social.Notification;

import java.util.List;

/**
 * Defines various events in the scoreboard lifecycle
 *
 * @author Roman Smetana
 */
public interface ScoreboardListener {
    /**
     * Event triggered on a new team submission creation
     *
     * @param teamProblem created team submission
     * @param effectedTeams teams effected by {@code teamProblem}
     */
    void onSubmission(final TeamProblem teamProblem, final List<Team> effectedTeams);

    /**
     * Event triggered on pending team submission
     *
     * @param teamProblem team submission
     */
    void onPendingSubmissionNotification(final TeamProblem teamProblem);

    /**
     * Event triggered on successful team submission
     *
     * @param teamProblem team submission
     */
    void onSuccessSubmissionNotification(final TeamProblem teamProblem);

    /**
     * Event triggered on failed team submission
     *
     * @param teamProblem team submission
     */
    void onFailedSubmissionNotification(final TeamProblem teamProblem);
}
