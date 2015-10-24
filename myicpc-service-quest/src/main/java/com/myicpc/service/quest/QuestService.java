package com.myicpc.service.quest;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.myicpc.dto.quest.QuestSubmissionDTO;
import com.myicpc.enums.ContestParticipantRole;
import com.myicpc.enums.NotificationType;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.contest.QuestConfiguration;
import com.myicpc.model.quest.QuestChallenge;
import com.myicpc.model.quest.QuestParticipant;
import com.myicpc.model.quest.QuestSubmission;
import com.myicpc.model.social.Notification;
import com.myicpc.model.teamInfo.ContestParticipant;
import com.myicpc.model.teamInfo.ContestParticipantAssociation;
import com.myicpc.repository.quest.QuestParticipantRepository;
import com.myicpc.repository.quest.QuestSubmissionRepository;
import com.myicpc.repository.social.NotificationRepository;
import com.myicpc.repository.teamInfo.ContestParticipantAssociationRepository;
import com.myicpc.service.utils.lists.NotificationList;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service responsible for Quest actions on public pages
 *
 * @author Roman Smetana
 */
@Service
public class QuestService {
    public static final int POSTS_PER_PAGE = 30;

    @Autowired
    private QuestParticipantRepository questParticipantRepository;

    @Autowired
    private QuestSubmissionRepository questSubmissionRepository;

    @Autowired
    private ContestParticipantAssociationRepository contestParticipantAssociationRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    /**
     * Notification types displayed on the quest timeline
     */
    public static final List<NotificationType> QUEST_TIMELINE_TYPES = NotificationList.newList()
            .addTwitter()
            .addInstagram()
            .addVine();

    /**
     * Finds first {@link QuestService#POSTS_PER_PAGE} newest notifications about Quest
     * <p/>
     * It searches the notifications for hashtag starting  with {@link QuestConfiguration#getHashtagPrefix()} and
     * the notification type is in {@link QuestService#QUEST_TIMELINE_TYPES}
     *
     * @param contest contest
     * @return quest notifications
     */
    public List<Notification> getQuestNotifications(final Contest contest) {
        if (contest == null || contest.getQuestConfiguration() == null || StringUtils.isEmpty(contest.getQuestConfiguration().getHashtagPrefix())) {
            return Collections.EMPTY_LIST;
        }
        Pageable pageable = new PageRequest(0, POSTS_PER_PAGE);
        Page<Notification> questNotifications = notificationRepository.findByHashtagAndNotificationTypes(
                String.format("%%|%s%%", contest.getQuestConfiguration().getHashtagPrefix()),
                QUEST_TIMELINE_TYPES,
                contest,
                pageable);
        return questNotifications.getContent();
    }

    /**
     * Finds a {@link QuestParticipant} by his username in social network
     * <p/>
     * It searches Twitter, Vine, and Instagram in this order. It returns the first match
     *
     * @param twitterUsername   twitter username
     * @param vineUsername      vine username
     * @param instagramUsername instagram username
     * @param contest           contest
     * @return quest participant matchint the searched username, or {@code null} if no match found
     */
    public QuestParticipant getQuestParticipantBySocialUsernames(String twitterUsername, String vineUsername, String instagramUsername, final Contest contest) {
        QuestParticipant questParticipant = null;
        if (StringUtils.isNotEmpty(twitterUsername)) {
            questParticipant = questParticipantRepository.findByContestAndContestParticipantTwitterUsernameIgnoreCase(contest, twitterUsername);
        }
        if (questParticipant == null && StringUtils.isNotEmpty(vineUsername)) {
            questParticipant = questParticipantRepository.findByContestAndContestParticipantVineUsernameIgnoreCase(contest, vineUsername);
        }
        if (questParticipant == null && StringUtils.isNotEmpty(instagramUsername)) {
            questParticipant = questParticipantRepository.findByContestAndContestParticipantInstagramUsernameIgnoreCase(contest, instagramUsername);
        }
        return questParticipant;
    }

    /**
     * Finds {@link QuestParticipant}s which are in {@code roles}
     * <p/>
     * It assigns to every quest participant his quest submissions and count number of accepted submissions
     *
     * @param roles   list of roles, the quest participant must have at least one
     * @param contest contest
     * @return quest participants in {@code roles}
     */
    @Transactional(readOnly = true)
    public List<QuestParticipant> getParticipantsWithRoles(final List<ContestParticipantRole> roles, final Contest contest) {
        if (roles == null) {
            return Collections.EMPTY_LIST;
        }
        int maxContestParticipantRoles = ContestParticipantRole.values().length;
        List<QuestParticipant> participants;
        // if no roles specified or all roles chosen, then search by contest, else by roles
        if (roles.isEmpty() || roles.size() == maxContestParticipantRoles) {
            participants = questParticipantRepository.findByContestOrderByPointsDescContestParticipantFirstnameAsc(contest);
        } else {
            participants = questParticipantRepository.findByRoles(roles, contest);
        }
        // skip if no participants found
        if (participants.isEmpty()) {
            return Collections.EMPTY_LIST;
        }

        // list of all involved contest participants
        List<ContestParticipant> contestParticipants = new ArrayList<>();
        // map between contest participant ID and quest participant
        Map<Long, QuestParticipant> participantsMap = new HashMap<>();
        // participants ids
        List<Long> participantIds = new ArrayList<>(participants.size());
        for (QuestParticipant participant : participants) {
            contestParticipants.add(participant.getContestParticipant());
            participantsMap.put(participant.getContestParticipant().getId(), participant);
            participantIds.add(participant.getId());
        }
        // add contest roles to quest participants
        List<ContestParticipantAssociation> associations = contestParticipantAssociationRepository.findByContestParticipantInAndContest(contestParticipants, contest);
        for (ContestParticipantAssociation association : associations) {
            participantsMap.get(association.getContestParticipant().getId()).addContestParticipantRole(association.getContestParticipantRole());
        }
        // finds quest participants submissions
        List<QuestSubmissionDTO> submissions = questSubmissionRepository.findQuestSubmissionDTOByQuestParticipantId(participantIds, contest);
        // map between quest participant ID and list of his submissions
        Multimap<Long, QuestSubmissionDTO> submissionMap = HashMultimap.create();
        for (QuestSubmissionDTO submission : submissions) {
            submissionMap.put(submission.getQuestParticipantId(), submission);
        }

        // count number of accepted submissions per quest participant
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

    /**
     * Sets contest quest prefix to all challenges in {@code challenges}
     * <p>
     * This is workaround to lazy loading of a complicated join to improve performance
     *
     * @param hashtagPrefix contest quest prefix
     * @param challenges challenges, where the prefix is applied
     */
    public static void applyHashtagPrefix(String hashtagPrefix, final List<QuestChallenge> challenges) {
        if (challenges != null && StringUtils.isNotEmpty(hashtagPrefix)) {
            for (QuestChallenge challenge : challenges) {
                challenge.setHashtagPrefix(hashtagPrefix);
            }
        }
    }

    /**
     * Sets contest quest prefix to all challenges in {@code submissions}
     * <p>
     * This is workaround to lazy loading of a complicated join to improve performance
     *
     * @param hashtagPrefix contest quest prefix
     * @param submissions submissions, to which challenges the prefix is applied
     */
    public static void applyHashtagPrefixToSubmissions(String hashtagPrefix, final List<QuestSubmission> submissions) {
        if (submissions != null && StringUtils.isNotEmpty(hashtagPrefix)) {
            for (QuestSubmission submission : submissions) {
                if (submission.getChallenge() != null) {
                    submission.getChallenge().setHashtagPrefix(hashtagPrefix);
                }
            }
        }
    }

}
