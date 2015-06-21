package com.myicpc.master.dao;

import com.myicpc.model.IdGeneratedObject;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Judgement;
import com.myicpc.model.eventFeed.Language;
import com.myicpc.model.eventFeed.LastTeamProblem;
import com.myicpc.model.eventFeed.Problem;
import com.myicpc.model.eventFeed.Region;
import com.myicpc.model.eventFeed.Team;
import com.myicpc.model.eventFeed.TeamProblem;
import com.myicpc.model.teamInfo.TeamInfo;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import java.util.List;

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

        // delete notifications generated from event feed
        deleteContestEntity("DELETE FROM Notification n WHERE n.contest.id = :contestId " +
                "AND n.notificationType IN (" +
                    "'SCOREBOARD_SUCCESS'," +
                    "'SCOREBOARD_FAILED'," +
                    "'SCOREBOARD_SUBMIT'," +
                    "'ANALYST_TEAM_MESSAGE'," +
                    "'ANALYST_MESSAGE'" +
                ")", contest.getId());
    }

    public <T extends IdGeneratedObject> T saveContestEntity(T object) {
        T merged = em.merge(object);
        em.flush();
        return merged;
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

    public Contest findContestById(Long contestId) {
        return em.find(Contest.class, contestId);
    }

    public Language findLanguageByName(String languageName) {
        List<Language> list = em.createQuery("FROM Language WHERE name = :languageName", Language.class)
                .setParameter("languageName", languageName)
                .getResultList();
        return getFirstElement(list);
    }

    public Region findRegionByNameAndContest(String regionName, Contest contest) {
        List<Region> list = em.createQuery("FROM Region WHERE name = :regionName AND contest.id = :contestId", Region.class)
                .setParameter("regionName", regionName)
                .setParameter("contestId", contest.getId())
                .getResultList();
        return getFirstElement(list);
    }

    public Judgement findJudgementByCodeAndContest(String judgementCode, Contest contest) {
        List<Judgement> list = em.createQuery("FROM Judgement WHERE code = :judgementCode AND contest.id = :contestId", Judgement.class)
                .setParameter("judgementCode", judgementCode)
                .setParameter("contestId", contest.getId())
                .getResultList();
        return getFirstElement(list);
    }

    public Problem findProblemBySystemIdAndContest(Long systemId, Contest contest) {
        List<Problem> list = em.createQuery("FROM Problem WHERE systemId = :systemId AND contest.id = :contestId", Problem.class)
                .setParameter("systemId", systemId)
                .setParameter("contestId", contest.getId())
                .getResultList();
        return getFirstElement(list);
    }

    public Team findTeamBySystemIdAndContest(Long systemId, Contest contest) {
        List<Team> list = em.createQuery("FROM Team WHERE systemId = :systemId AND contest.id = :contestId", Team.class)
                .setParameter("systemId", systemId)
                .setParameter("contestId", contest.getId())
                .getResultList();
        return getFirstElement(list);
    }

    public List<Team> findTeamByContest(Contest contest) {
        return em.createQuery("FROM Team WHERE contest = :contest")
                .setParameter("contest", contest)
                .getResultList();
    }

    public TeamInfo findTeamInfoByExternalIdAndContest(Long externalId, Contest contest) {
        List<TeamInfo> list =  em.createQuery("FROM TeamInfo WHERE externalId = :externalId AND contest.id = :contestId", TeamInfo.class)
                .setParameter("externalId", externalId)
                .setParameter("contestId", contest.getId())
                .getResultList();
        return getFirstElement(list);
    }

    public TeamProblem findTeamProblemBySystemIdAndTeamIdAndProblemId(Long systemId, Long teamId, Long problemId) {
        List<TeamProblem> list = em.createQuery("FROM TeamProblem WHERE systemId = :systemId AND team.id = :teamId AND problem.id = :problemId", TeamProblem.class)
                .setParameter("systemId", systemId)
                .setParameter("teamId", teamId)
                .setParameter("problemId", problemId)
                .getResultList();
        return getFirstElement(list);
    }

    public TeamProblem findTeamProblemBySystemIdAndTeamContest(Long systemId, Contest contest) {
        List<TeamProblem> list =  em.createQuery("FROM TeamProblem WHERE systemId = :systemId AND team.contest.id = :contestId", TeamProblem.class)
                .setParameter("systemId", systemId)
                .setParameter("contestId", contest.getId())
                .getResultList();
        return getFirstElement(list);
    }

    public List<TeamProblem> findTeamProblemByTeamAndProblemOrderByTimeAsc(Team team, Problem problem) {
        return em.createQuery("FROM TeamProblem WHERE team = :team AND problem = :problem ORDER BY time ASC", TeamProblem.class)
                .setParameter("team", team)
                .setParameter("problem", problem)
                .getResultList();
    }

    public LastTeamProblem findLastTeamProblemByTeamAndProblem(Team team, Problem problem) {
        List<LastTeamProblem> list =  em.createQuery("FROM LastTeamProblem WHERE team = :team AND problem = :problem", LastTeamProblem.class)
                .setParameter("team", team)
                .setParameter("problem", problem)
                .getResultList();
        return getFirstElement(list);
    }

    public List<LastTeamProblem> findLastTeamProblemByTeam(Team team) {
        return em.createQuery("FROM LastTeamProblem WHERE team = :team", LastTeamProblem.class)
                .setParameter("team", team)
                .getResultList();
    }

    public Double getLastAcceptedTeamProblemTime(Team team) {
        List<Double> list = em.createQuery("SELECT MIN(tp.time) FROM TeamProblem tp WHERE tp.team = :team AND tp.solved = true", Double.class)
                .setParameter("team", team)
                .getResultList();
        return getFirstElement(list);
    }

    protected <T> T getFirstElement(List<T> list) {
        if (list == null || list.size() != 1) {
            return null;
        }
        return list.get(0);
    }
}
