package com.myicpc.service.quest;

import com.myicpc.model.quest.QuestChallenge;
import com.myicpc.model.quest.QuestParticipant;
import com.myicpc.repository.quest.QuestChallengeRepository;
import com.myicpc.repository.quest.QuestParticipantRepository;
import com.myicpc.service.exception.BusinessValidationException;
import com.myicpc.service.validation.QuestChallengeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Roman Smetana
 */
@Service
@Transactional
public class QuestMngmService {

    @Autowired
    private QuestChallengeRepository challengeRepository;

    @Autowired
    private QuestParticipantRepository questParticipantRepository;

    @Autowired
    private QuestChallengeValidator challengeValidator;

    public void updateQuestChallenge(QuestChallenge challenge) throws BusinessValidationException {
        challengeValidator.validate(challenge);
        challenge = challengeRepository.save(challenge);

        // TODO generate schedule notification
//        List<Notification> notifications = notificationRepository.findByEntityIdAndNotificationType(challenge.getId(),
//                NotificationType.QUEST_CHALLENGE);
//        for (Notification notification : notifications) {
//            notification.setTitle(challenge.getName());
//            notification.setBody(challenge.getNotificationDescription());
//            notification.setUrl(challenge.getImageURL());
//            notification.setCode("{\"hashtag\":\"" + challenge.getHashtag() + "\"}");
//        }
//        notificationRepository.save(notifications);
//        if (challenge.isPublished()) {
//            PublishService.broadcastNotification(notificationService.notificationForQuestChallenge(challenge));
//        }
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

        Iterable<QuestParticipant> participants = questParticipantRepository.findAll();
        for (QuestParticipant participant : participants) {
            participant.calcQuestPoints();
        }
        questParticipantRepository.save(participants);

        // TODO send notification
//        notificationService.deleteNoficicationsForEntity(questChallenge.getId(), NotificationType.QUEST_CHALLENGE);
    }
}
