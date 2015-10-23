package com.myicpc.service.quest;

import com.myicpc.enums.ContestParticipantRole;
import com.myicpc.enums.NotificationType;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.quest.QuestChallenge;
import com.myicpc.model.quest.QuestParticipant;
import com.myicpc.model.quest.QuestSubmission;
import com.myicpc.model.quest.QuestSubmission.QuestSubmissionState;
import com.myicpc.model.social.Notification;
import com.myicpc.repository.quest.QuestChallengeRepository;
import com.myicpc.repository.quest.QuestParticipantRepository;
import com.myicpc.repository.quest.QuestSubmissionRepository;
import com.myicpc.repository.social.NotificationRepository;
import com.myicpc.service.exception.BusinessValidationException;
import com.myicpc.service.notification.NotificationBuilder;
import com.myicpc.service.notification.NotificationService;
import com.myicpc.service.publish.PublishService;
import com.myicpc.service.validation.QuestChallengeValidator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Management CRUD service for Quest entities
 *
 * @author Roman Smetana
 */
@Service
@Transactional
public class QuestMngmService {
    private static final Logger logger = LoggerFactory.getLogger(QuestMngmService.class);

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

    @Autowired
    private NotificationService notificationService;

    /**
     * Saves quest challenge updates
     *
     * @param challenge quest challenge to be updated
     * @return updated quest challenge
     * @throws BusinessValidationException quest challenge validation failed
     */
    public QuestChallenge updateQuestChallenge(final QuestChallenge challenge) throws BusinessValidationException {
        challengeValidator.validate(challenge);
        QuestChallenge updatedChallenge = challengeRepository.save(challenge);

        notificationService.modifyExistingNotifications(challenge, NotificationType.QUEST_CHALLENGE, new NotificationService.NotificationModifier() {
            @Override
            public void modify(Notification notification) {
                notification.setTitle(challenge.getName());
                notification.setBody(challenge.getNotificationDescription());
                notification.setImageUrl(challenge.getImageURL());
                notification.setHashtags(challenge.getHashtag());
            }
        });

        return updatedChallenge;
    }

    /**
     * Delete Quest challenge and recalculate Quest participants points
     *
     * @param questChallenge challenge to delete
     */
    public void deleteQuestChallenge(final QuestChallenge questChallenge) {
        if (questChallenge == null) {
            return;
        }
        List<QuestSubmission> submissions = submissionRepository.findByChallenge(questChallenge);
        submissionRepository.delete(submissions);

        challengeRepository.delete(questChallenge);
        challengeRepository.flush();

        Iterable<QuestParticipant> participants = participantRepository.findByContest(questChallenge.getContest());
        for (QuestParticipant participant : participants) {
            participant.calcQuestPoints();
        }
        participantRepository.save(participants);

        notificationService.deleteExistingNotifications(questChallenge, NotificationType.QUEST_CHALLENGE);
    }

    /**
     * Bulk updates of {@link QuestParticipant}
     *
     * @param params map between {@link QuestParticipant#id} and adjustment points
     * @param contest contest
     */
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
     * Accept a Quest submission
     *
     * @param submission  submission
     * @param questPoints points awarded the submission
     * @param contest     contest
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
     *
     * @param submission     submission
     * @param reasonToReject text explaining why the submission was rejected
     * @param contest        contest
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

    /**
     * Finds all existing {@link ContestParticipantRole}s and converts them to {@link List}
     *
     * @return all {@link ContestParticipantRole}s list
     */
    public List<ContestParticipantRole> getAllContestParticipantRoles() {
        return Arrays.asList(ContestParticipantRole.values());
    }

    /**
     * Finds all open quest challenges, which have not been published yet, and publish them
     *
     * @param contest contest
     */
    @Transactional
    public void createNotificationsForNewQuestChallenges(Contest contest) {
        List<QuestChallenge> challenges = challengeRepository.findAllNonpublishedStartedChallenges(new Date(), contest);
        for (QuestChallenge challenge : challenges) {
            challenge.setPublished(true);

            NotificationBuilder builder = new NotificationBuilder(challenge);
            builder.setTitle(challenge.getName());
            builder.setBody(challenge.getDescription());
            builder.setImageUrl(challenge.getImageURL());
            builder.setNotificationType(NotificationType.QUEST_CHALLENGE);
            builder.setContest(contest);
            challenge.setHashtagPrefix(contest.getQuestConfiguration().getHashtagPrefix());
            builder.setHashtag(challenge.getHashtag());
            Notification notification = notificationRepository.save(builder.build());
            publishService.broadcastNotification(notification, contest);
        }
    }
}
