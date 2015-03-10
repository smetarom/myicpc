package com.myicpc.service.quest;

import com.myicpc.enums.ContestParticipantRole;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.quest.QuestChallenge;
import com.myicpc.model.quest.QuestParticipant;
import com.myicpc.model.quest.QuestSubmission;
import com.myicpc.model.quest.QuestSubmission.QuestSubmissionState;
import com.myicpc.repository.quest.QuestChallengeRepository;
import com.myicpc.repository.quest.QuestParticipantRepository;
import com.myicpc.repository.quest.QuestSubmissionRepository;
import com.myicpc.service.EntityManagerService;
import com.myicpc.service.exception.BusinessValidationException;
import com.myicpc.service.quest.dto.QuestSubmissionFilter;
import com.myicpc.service.validation.QuestChallengeValidator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
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

    @Autowired
    private QuestChallengeRepository challengeRepository;

    @Autowired
    private QuestParticipantRepository participantRepository;

    @Autowired
    private QuestSubmissionRepository submissionRepository;

    @Autowired
    private QuestChallengeValidator challengeValidator;

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

        q.select(c).where(predicate).orderBy(cb.asc(c.<Date>get("created")));
        List<QuestSubmission> list = em.createQuery(q).setFirstResult(pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();

        countQ.select(cb.count(countRoot)).where(countPredicate);
        long totalCount = em.createQuery(countQ).getSingleResult();

        return new PageImpl<>(list, pageable, totalCount);
    }

    /**
     * Accept a Quest submission
     *
     * @param submission
     *            submission
     * @param questPoints
     *            number of points awarded for this submission
     */
    public void acceptQuestSubmission(QuestSubmission submission, final Integer questPoints) {
        submission.setReasonToReject(null);
        submission.setSubmissionState(QuestSubmissionState.ACCEPTED);
        submission.setQuestPoints(questPoints);
        submission = submissionRepository.save(submission);

        submission.getParticipant().calcQuestPoints();
        participantRepository.save(submission.getParticipant());

        // TODO broadcast a submission?
//        PublishService.broadcastQuestSubmission(submission);
    }

    /**
     * Reject a Quest submission
     *
     * @param submission
     *            submission
     * @param reasonToReject
     *            reason, why submission was rejected
     */
    public void rejectQuestSubmission(final QuestSubmission submission, final String reasonToReject) {
        if (!StringUtils.isEmpty(reasonToReject)) {
            submission.setReasonToReject(reasonToReject);
        }
        submission.setSubmissionState(QuestSubmissionState.REJECTED);
        submission.setQuestPoints(0);
        submissionRepository.save(submission);

        submission.getParticipant().calcQuestPoints();
        participantRepository.save(submission.getParticipant());

        // TODO broadcast a submission?
//        PublishService.broadcastQuestSubmission(submission);
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
}
