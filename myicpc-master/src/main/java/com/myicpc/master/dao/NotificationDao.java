package com.myicpc.master.dao;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Team;
import com.myicpc.model.eventFeed.TeamProblem;

import javax.ejb.Stateless;

/**
 * @author Roman Smetana
 */
@Stateless
public class NotificationDao extends GeneralDao {
    public long getAnalystSubmissionMessageCount(final TeamProblem teamProblem, String message) {
        return em.createQuery("SELECT COUNT(n) FROM Notification n WHERE n.entityId = :entityId " +
                "AND n.contest.id = :contestId " +
                "AND (n.notificationType = 'SCOREBOARD_SUCCESS' OR n.notificationType = 'SCOREBOARD_FAILED' OR n.notificationType = 'SCOREBOARD_SUBMIT') " +
                "AND n.body = :body", Long.class)
                .setParameter("entityId", teamProblem.getId())
                .setParameter("contestId", teamProblem.getTeam().getContest().getId())
                .setParameter("body", message)
                .getSingleResult();
    }

    public long getAnalystTeamMessageCount(final Team team, String message) {
        return em.createQuery("SELECT COUNT(n) FROM Notification n WHERE n.entityId = :entityId " +
                "AND n.contest.id = :contestId " +
                "AND n.notificationType = 'ANALYST_TEAM_MESSAGE' " +
                "AND n.body = :body", Long.class)
                .setParameter("entityId", team.getId())
                .setParameter("contestId", team.getContest().getId())
                .setParameter("body", message)
                .getSingleResult();
    }

    public long getAnalystGeneralMessageCount(final Contest contest, String message) {
        return em.createQuery("SELECT COUNT(n) FROM Notification n WHERE n.entityId IS NULL " +
                    "AND n.contest.id = :contestId " +
                    "AND n.notificationType = 'ANALYST_MESSAGE' " +
                    "AND n.body = :body", Long.class)
                .setParameter("contestId", contest.getId())
                .setParameter("body", message)
                .getSingleResult();
    }
}
