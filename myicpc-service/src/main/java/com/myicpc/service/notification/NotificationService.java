package com.myicpc.service.notification;

import com.myicpc.model.eventFeed.Team;
import com.myicpc.model.eventFeed.TeamProblem;
import com.myicpc.model.social.Notification;
import com.myicpc.service.listener.ScoreboardListener;

import java.util.List;

/**
 * @author Roman Smetana
 */
public interface NotificationService extends ScoreboardListener {
    @Override
    void onPendingSubmissionNotification(TeamProblem teamProblem);

    @Override
    void onSuccessSubmissionNotification(TeamProblem teamProblem);

    @Override
    void onFailedSubmissionNotification(TeamProblem teamProblem);

    @Override
    void onSubmission(TeamProblem teamProblem, List<Team> effectedTeams);

    public List<Notification> getFeaturedNotifications(final List<Long> ignoredFeatured);
}
