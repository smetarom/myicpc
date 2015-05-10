package com.myicpc.master.dao;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.quest.QuestChallenge;

import javax.ejb.Stateless;
import java.util.Date;
import java.util.List;

/**
 * @author Roman Smetana
 */
@Stateless
public class QuestDao extends GeneralDao {
    public List<QuestChallenge> findAllNonpublishedStartedChallenges(final Contest contest) {
        return em.createQuery("FROM QuestChallenge qc WHERE " +
                "qc.published = false " +
                "AND qc.startDate < :now " +
                "AND qc.contest.id = :contestId", QuestChallenge.class)
                .setParameter("now", new Date())
                .setParameter("contestId", contest.getId())
                .getResultList();
    }
}
