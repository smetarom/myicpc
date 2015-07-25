package com.myicpc.repository.quest;

import com.myicpc.dto.quest.QuestSubmissionFilter;
import com.myicpc.enums.SortOrder;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.quest.QuestChallenge;
import com.myicpc.model.quest.QuestParticipant;
import com.myicpc.model.quest.QuestSubmission;
import com.myicpc.model.social.Notification;
import com.myicpc.repository.AbstractDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

/**
 * Implementation of custom DAO methods from {@link QuestSubmissionDAO}
 * and include them to {@link QuestSubmissionRepository}
 *
 * @author Roman Smetana
 */
public class QuestSubmissionRepositoryImpl extends AbstractDao implements QuestSubmissionDAO {

    @Override
    public Page<QuestSubmission> getFiltredQuestSumbissions(final QuestSubmissionFilter submissionFilter, final Contest contest, final Pageable pageable) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        // content
        CriteriaQuery<QuestSubmission> q = cb.createQuery(QuestSubmission.class);
        Root<QuestSubmission> c = q.from(QuestSubmission.class);
        Join<QuestSubmission, QuestChallenge> challengeJoin = c.join("challenge");
        Join<QuestSubmission, Notification> notificationJoin = c.join("notification");
        // total count
        CriteriaQuery<Long> countQ = cb.createQuery(Long.class);
        Root<QuestSubmission> countRoot = countQ.from(QuestSubmission.class);
        Join<QuestSubmission, QuestChallenge> countChallengeJoin = countRoot.join("challenge");

        Predicate predicate = cb.equal(challengeJoin.<Long>get("contest"), contest);
        Predicate countPredicate = cb.equal(countChallengeJoin.<Long>get("contest"), contest);
        if (submissionFilter.getSubmissionState() != null) {
            predicate = cb.and(predicate, cb.equal(c.get("submissionState"), submissionFilter.getSubmissionState()));
            countPredicate = cb.and(countPredicate, cb.equal(countRoot.get("submissionState"), submissionFilter.getSubmissionState()));
        }
        if (submissionFilter.getChallenge() != null) {
            predicate = cb.and(predicate, cb.equal(c.<QuestChallenge>get("challenge"), submissionFilter.getChallenge()));
            countPredicate = cb.and(countPredicate, cb.equal(countRoot.<QuestChallenge>get("challenge"), submissionFilter.getChallenge()));
        }
        if (submissionFilter.getParticipant() != null) {
            predicate = cb.and(predicate, cb.equal(c.<QuestParticipant>get("participant"), submissionFilter.getParticipant()));
            countPredicate = cb.and(countPredicate, cb.equal(countRoot.<QuestParticipant>get("participant"), submissionFilter.getParticipant()));
        }

        Path<Date> orderBy = notificationJoin.get("timestamp");
        Order order = cb.asc(orderBy);
        if (submissionFilter.getSortOrder() == SortOrder.DESC) {
            order = cb.desc(orderBy);
        }

        q.select(c).where(predicate).orderBy(order);
        List<QuestSubmission> list = getEntityManager().createQuery(q).setFirstResult(pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();

        countQ.select(cb.count(countRoot)).where(countPredicate);
        long totalCount = getEntityManager().createQuery(countQ).getSingleResult();

        return new PageImpl<>(list, pageable, totalCount);
    }
}
