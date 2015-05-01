package com.myicpc.master.dao;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Problem;
import com.myicpc.model.eventFeed.Team;

import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 * @author Roman Smetana
 */
@Stateless
public class EventFeedDao extends GeneralDao {

    public void deleteAllEventFeedData(final Contest contest) {
        deleteContestEntity("DELETE FROM LastTeamProblem ltp WHERE ltp.team IN (SELECT t FROM Team t WHERE t.contest.id = :contestId)", contest.getId());
        deleteContestEntity("DELETE FROM TeamProblem tp WHERE tp.team IN (SELECT t FROM Team t WHERE t.contest.id = :contestId)", contest.getId());
        deleteContestEntity("DELETE FROM TeamRankHistory trh WHERE trh.team IN (SELECT t FROM Team t WHERE t.contest.id = :contestId)", contest.getId());
        deleteContestEntity("DELETE FROM CodeInsightActivity ea WHERE ea.team IN (SELECT t FROM Team t WHERE t.contest.id = :contestId)", contest.getId());
        deleteContestEntity("DELETE FROM Problem p WHERE p.contest.id = :contestId", contest.getId());
        deleteContestEntity("DELETE FROM Team t WHERE t.contest.id = :contestId", contest.getId());
        deleteContestEntity("DELETE FROM Region r WHERE r.contest.id = :contestId", contest.getId());
        deleteContestEntity("DELETE FROM Judgement j WHERE j.contest.id = :contestId", contest.getId());

        // TODO delete notifications
    }

    private int deleteContestEntity(String hql, Long contestId) {
        return em.createQuery(hql).setParameter("contestId", contestId).executeUpdate();
    }

    public Team getTeamBySystemId(String systemId, Long contestId) {
        try {
            long teamId = Long.parseLong(systemId);
            TypedQuery<Team> query = em.createQuery("FROM Team t WHERE t.systemId = :systemId AND t.contest.id = :contestId", Team.class);
            query.setParameter("systemId", teamId);
            query.setParameter("contestId", contestId);
            return query.getSingleResult();
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Problem getProblemBySystemId(String systemId, Long contestId) {
        try {
            long problemId = Long.parseLong(systemId);
            TypedQuery<Problem> query = em.createQuery("FROM Problem p WHERE p.systemId = :systemId AND p.contest.id = :contestId", Problem.class);
            query.setParameter("systemId", problemId);
            query.setParameter("contestId", contestId);
            return query.getSingleResult();
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
