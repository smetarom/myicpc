package com.myicpc.service.listener;

import com.myicpc.model.eventFeed.Team;
import com.myicpc.model.eventFeed.TeamProblem;
import com.myicpc.model.social.Notification;

import java.util.List;

/**
 * @author Roman Smetana
 */
public interface ScoreboardListener {
    void onSubmission(final TeamProblem teamProblem, final List<Team> effectedTeams);
    void onPendingSubmissionNotification(final TeamProblem teamProblem);
    void onSuccessSubmissionNotification(final TeamProblem teamProblem);
    void onFailedSubmissionNotification(final TeamProblem teamProblem);
}
