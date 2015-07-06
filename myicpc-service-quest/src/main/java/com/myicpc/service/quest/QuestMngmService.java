package com.myicpc.service.quest;

import com.myicpc.enums.ContestParticipantRole;
import com.myicpc.enums.NotificationType;
import com.myicpc.enums.SortOrder;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.quest.QuestChallenge;
import com.myicpc.model.quest.QuestParticipant;
import com.myicpc.model.quest.QuestSubmission;
import com.myicpc.model.quest.QuestSubmission.QuestSubmissionState;
import com.myicpc.model.social.Notification;
import com.myicpc.repository.contest.ContestRepository;
import com.myicpc.repository.quest.QuestChallengeRepository;
import com.myicpc.repository.quest.QuestParticipantRepository;
import com.myicpc.repository.quest.QuestSubmissionRepository;
import com.myicpc.repository.social.NotificationRepository;
import com.myicpc.service.EntityManagerService;
import com.myicpc.service.exception.BusinessValidationException;
import com.myicpc.service.publish.PublishService;
import com.myicpc.service.quest.dto.QuestSubmissionFilter;
import com.myicpc.service.validation.QuestChallengeValidator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Roman Smetana
 */
@Service
@Transactional
public class QuestMngmService extends EntityManagerService {
    private static final Logger logger = LoggerFactory.getLogger(QuestMngmService.class);

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private QuestChallengeRepository challengeRepository;

    @Autowired
    private QuestParticipantRepository participantRepository;

    @Autowired
    private QuestSubmissionRepository submissionRepository;

    @Autowired
    private QuestChallengeValidator challengeValidator;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private PublishService publishService;

    public QuestChallenge updateQuestChallenge(final QuestChallenge challenge) throws BusinessValidationException {
        challengeValidator.validate(challenge);
        QuestChallenge updatedChallenge = challengeRepository.save(challenge);

        // TODO generate schedule notification
//        List<Notification> notifications = notificationRepository.findByEntityIdAndNotificationType(challenge.getId(),
//                NotificationType.QUEST_CHALLENGE);
//        for (Notification notification : notifications) {
//            notification.setTitle(challenge.getName());
//            notification.setBody(challenge.getNotificationDescription());
//            notification.setUrl(challenge.getImageUrl());
//            notification.setCode("{\"hashtag\":\"" + challenge.getHashtag() + "\"}");
//        }
//        notificationRepository.save(notifications);
//        if (challenge.isPublished()) {
//            PublishService.broadcastNotification(notificationService.notificationForQuestChallenge(challenge));
//        }

        return updatedChallenge;
    }

    /**
     * Delete Quest challenge and recalculate Quest participants points
     *
     * @param questChallenge
     *            challenge to delete
     */
    public void deleteQuestChallenge(final QuestChallenge questChallenge) {
        if (questChallenge == null) {
            return;
        }
        challengeRepository.delete(questChallenge);

        challengeRepository.flush();

        Iterable<QuestParticipant> participants = participantRepository.findAll();
        for (QuestParticipant participant : participants) {
            participant.calcQuestPoints();
        }
        participantRepository.save(participants);

        // TODO send notification
//        notificationService.deleteNoficicationsForEntity(questChallenge.getId(), NotificationType.QUEST_CHALLENGE);
    }

    public void bulkParticipantUpdate(final Map<String, String[]> params, final Contest contest) {
        Iterable<QuestParticipant> list = participantRepository.findByContest(contest);
        Map<Long, QuestParticipant> participantCache = new HashMap<>();
        for (QuestParticipant questParticipant : list) {
            participantCache.put(questParticipant.getId(), questParticipant);
        }

        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            String key = entry.getKey();
            if (entry.getValue() != null &&
                    entry.getValue().length == 1 &&
                    !StringUtils.isEmpty(entry.getValue()[0])) {
                try {
                    String value = entry.getValue()[0];
                    Long participantId = Long.parseLong(key);
                    QuestParticipant participant = participantCache.get(participantId);
                    if (participant != null) {
                        participant.setPointsAdjustment(NumberUtils.toInt(value.trim()));
                        participantRepository.save(participant);
                    }
                } catch (NumberFormatException e) {
                    // Invalid input, ignore
                }
            }
        }
    }

    /**
     * Filter Quest submissions by {@link QuestSubmissionFilter}
     *
     * @param submissionFilter
     *            submission filter
     * @param pageable
     *            page controller
     * @return filtered page of Quest notifications
     */
    public Page<QuestSubmission> getFiltredQuestSumbissions(final QuestSubmissionFilter submissionFilter, final Contest contest,
                                                            final Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

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
        List<QuestSubmission> list = em.createQuery(q).setFirstResult(pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();

        countQ.select(cb.count(countRoot)).where(countPredicate);
        long totalCount = em.createQuery(countQ).getSingleResult();

        return new PageImpl<>(list, pageable, totalCount);
    }

    /**
     * Accept a Quest submission
     *  @param submission
     *            submission
     * @param questPoints
     * @param contest
     */
    public void acceptQuestSubmission(final QuestSubmission submission, final Integer questPoints, Contest contest) {
        QuestSubmission submissionDB = submissionRepository.findOne(submission.getId());
        submissionDB.setReasonToReject(null);
        submissionDB.setSubmissionState(QuestSubmissionState.ACCEPTED);
        submissionDB.setQuestPoints(questPoints);
        submissionDB = submissionRepository.save(submissionDB);

        submissionDB.getParticipant().calcQuestPoints();
        participantRepository.save(submissionDB.getParticipant());

        publishService.broadcastQuestSubmission(submissionDB, contest.getCode());
    }

    /**
     * Reject a Quest submission
     *  @param submission
     *            submission
     * @param reasonToReject
     * @param contest
     */
    public void rejectQuestSubmission(final QuestSubmission submission, final String reasonToReject, Contest contest) {
        QuestSubmission submissionDB = submissionRepository.findOne(submission.getId());
        if (!StringUtils.isEmpty(reasonToReject)) {
            submissionDB.setReasonToReject(reasonToReject);
        }
        submissionDB.setSubmissionState(QuestSubmissionState.REJECTED);
        submissionDB.setQuestPoints(0);
        submissionDB = submissionRepository.save(submissionDB);

        submissionDB.getParticipant().calcQuestPoints();
        participantRepository.save(submissionDB.getParticipant());

        publishService.broadcastQuestSubmission(submissionDB, contest.getCode());
    }

    /**
     * @return all quest submission types as pairs: enum code, type name
     */
    public Map<String, String> getQuestSubmissionStates() {
        Map<String, String> map = new LinkedHashMap<>();
        for (QuestSubmissionState submissionState : QuestSubmissionState.values()) {
            map.put(submissionState.toString(), submissionState.getLabel());
        }
        return map;
    }

    public List<ContestParticipantRole> getAllContestParticipantRoles() {
        return Arrays.asList(ContestParticipantRole.values());
    }

    public void processReceivedNotification(final Notification receivedNotification) {
        Contest contest = contestRepository.getOne(receivedNotification.getContest().getId());
        Notification existingNotification = notificationRepository.findByContestAndExternalIdAndNotificationType(contest, receivedNotification.getExternalId(), NotificationType.VINE);

        if (existingNotification != null) {
            logger.info("Skip quest challenge " + receivedNotification.getExternalId() + " because of duplication.");
            return;
        }

        receivedNotification.setContest(contest);
        notificationRepository.save(receivedNotification);
        publishService.broadcastNotification(receivedNotification, contest);
    }
}
