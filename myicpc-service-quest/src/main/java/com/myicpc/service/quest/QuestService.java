package com.myicpc.service.quest;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.myicpc.dto.quest.QuestSubmissionDTO;
import com.myicpc.enums.ContestParticipantRole;
import com.myicpc.enums.NotificationType;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.quest.QuestChallenge;
import com.myicpc.model.quest.QuestParticipant;
import com.myicpc.model.quest.QuestSubmission;
import com.myicpc.model.social.Notification;
import com.myicpc.model.teamInfo.ContestParticipant;
import com.myicpc.model.teamInfo.ContestParticipantAssociation;
import com.myicpc.repository.quest.QuestChallengeRepository;
import com.myicpc.repository.quest.QuestParticipantRepository;
import com.myicpc.repository.quest.QuestSubmissionRepository;
import com.myicpc.repository.social.NotificationRepository;
import com.myicpc.repository.teamInfo.ContestParticipantAssociationRepository;
import com.myicpc.service.notification.NotificationBuilder;
import com.myicpc.service.publish.PublishService;
import com.myicpc.service.utils.lists.NotificationList;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Roman Smetana
 */
@Service
public class QuestService {
    public static final int POSTS_PER_PAGE = 30;

    @Autowired
    private PublishService publishService;

    @Autowired
    private QuestChallengeRepository questChallengeRepository;

    @Autowired
    private QuestParticipantRepository questParticipantRepository;

    @Autowired
    private QuestSubmissionRepository questSubmissionRepository;

    @Autowired
    private ContestParticipantAssociationRepository contestParticipantAssociationRepository;

    /**
     * Notification types displayed on the quest timeline
     */
    public static final List<NotificationType> QUEST_TIMELINE_TYPES = NotificationList.newList()
            .addTwitter()
            .addInstagram()
            .addVine();

    @Autowired
    private NotificationRepository notificationRepository;

    public List<Notification> getQuestNotifications(final Contest contest) {
        Pageable pageable = new PageRequest(0, POSTS_PER_PAGE);
        Page<Notification> questNotifications = notificationRepository.findByHashtagAndNotificationTypes(
                String.format("%%|%s%%", contest.getQuestConfiguration().getHashtagPrefix()),
                QUEST_TIMELINE_TYPES,
                contest,
                pageable);
        return questNotifications.getContent();
    }

    public QuestParticipant getQuestParticipantBySocialUsernames(String twitterUsername, String vineUsername, String instagramUsername, final Contest contest) {
        QuestParticipant questParticipant = null;
        if (questParticipant == null && StringUtils.isNotEmpty(twitterUsername)) {
            questParticipant = questParticipantRepository.findByContestAndContestParticipantTwitterUsernameIgnoreCase(contest, twitterUsername);
        }
        if (questParticipant == null && StringUtils.isNotEmpty(twitterUsername)) {
            questParticipant = questParticipantRepository.findByContestAndContestParticipantVineUsernameIgnoreCase(contest, vineUsername);
        }
        if (questParticipant == null && StringUtils.isNotEmpty(twitterUsername)) {
            questParticipant = questParticipantRepository.findByContestAndContestParticipantInstagramUsernameIgnoreCase(contest, instagramUsername);
        }
        return questParticipant;
    }

    @Transactional(readOnly = true)
    public List<QuestParticipant> getParticipantsWithRoles(final List<ContestParticipantRole> roles, final Contest contest, boolean extended) {
        List<QuestParticipant> participants = questParticipantRepository.findByRoles(roles, contest, null);
        if (participants.isEmpty()) {
            return participants;
        }
        List<ContestParticipant> contestParticipants = new ArrayList<>();
        Map<Long, QuestParticipant> participantsMap = new HashMap<>();
        List<Long> participantIds = new ArrayList<>(participants.size());
        for (QuestParticipant participant : participants) {
            contestParticipants.add(participant.getContestParticipant());
            participantsMap.put(participant.getContestParticipant().getId(), participant);
            participantIds.add(participant.getId());
        }
        List<ContestParticipantAssociation> associations = contestParticipantAssociationRepository.findByContestParticipantIn(contestParticipants);
        for (ContestParticipantAssociation association : associations) {
            participantsMap.get(association.getContestParticipant().getId()).addContestParticipantRole(association.getContestParticipantRole());
        }
        List<QuestSubmissionDTO> submissions = questSubmissionRepository.findQuestSubmissionDTOByQuestParticipantId(participantIds, contest);
        Multimap<Long, QuestSubmissionDTO> submissionMap = HashMultimap.create();
        for (QuestSubmissionDTO submission : submissions) {
            submissionMap.put(submission.getQuestParticipantId(), submission);
        }

        for (QuestParticipant participant : participants) {
            int acceptedSubmissions = 0;
            for (QuestSubmissionDTO questSubmissionDTO : submissionMap.get(participant.getId())) {
                participant.addSubmissionDTO(questSubmissionDTO);
                if (questSubmissionDTO.getSubmissionState() == QuestSubmission.QuestSubmissionState.ACCEPTED) {
                    acceptedSubmissions++;
                }
            }
            participant.setAcceptedSubmissions(acceptedSubmissions);
        }

        return participants;
    }

    public static void applyHashtagPrefix(String hashtagPrefix, final List<QuestChallenge> challenges) {
        if (challenges != null) {
            for (QuestChallenge challenge : challenges) {
                challenge.setHashtagPrefix(hashtagPrefix);
            }
        }
    }

    public static void applyHashtagPrefixToSubmissions(String hashtagPrefix, final List<QuestSubmission> submissions) {
        if (submissions != null) {
            for (QuestSubmission submission : submissions) {
                submission.getChallenge().setHashtagPrefix(hashtagPrefix);
            }
        }
    }

    @Transactional
    public void createNotificationsForNewQuestChallenges(Contest contest) {
        List<QuestChallenge> challenges = questChallengeRepository.findAllNonpublishedStartedChallenges(new Date(), contest);
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
