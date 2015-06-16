package com.myicpc.service.quest;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myicpc.dto.quest.QuestSubmissionDTO;
import com.myicpc.enums.ContestParticipantRole;
import com.myicpc.enums.NotificationType;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.quest.QuestChallenge;
import com.myicpc.model.quest.QuestParticipant;
import com.myicpc.model.quest.QuestSubmission;
import com.myicpc.model.social.Notification;
import com.myicpc.repository.quest.QuestParticipantRepository;
import com.myicpc.repository.quest.QuestSubmissionRepository;
import com.myicpc.repository.social.NotificationRepository;
import com.myicpc.service.utils.lists.NotificationList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Roman Smetana
 */
@Service
public class QuestService {
    public static final int POSTS_PER_PAGE = 30;

    @Autowired
    private QuestParticipantRepository questParticipantRepository;

    @Autowired
    private QuestSubmissionRepository questSubmissionRepository;

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

    public JsonArray getJSONChallenges(final List<QuestChallenge> challenges) {
        JsonArray arr = new JsonArray();
        for (QuestChallenge questChallenge : challenges) {
            JsonObject o = new JsonObject();
            o.addProperty("id", questChallenge.getId());
            o.addProperty("name", questChallenge.getHashtagSuffix());
            arr.add(o);
        }
        return arr;
    }

    public List<QuestParticipant> getParticipantsWithRoles(final List<ContestParticipantRole> roles, final Contest contest, boolean extended) {
        List<QuestParticipant> participants = questParticipantRepository.findByRoles(roles, contest, null);
        if (participants.isEmpty()) {
            return participants;
        }
        List<Long> participantIds = new ArrayList<>(participants.size());
        for (QuestParticipant participant : participants) {
            participantIds.add(participant.getId());
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

    public JsonArray getJSONParticipants(final List<ContestParticipantRole> roles, final Contest contest, boolean extended) {
        List<QuestParticipant> participants = questParticipantRepository.findByRoles(roles, contest, null);
        List<Long> participantIds = new ArrayList<>(participants.size());
        for (QuestParticipant participant : participants) {
            participantIds.add(participant.getId());
        }
        List<QuestSubmissionDTO> submissions = questSubmissionRepository.findQuestSubmissionDTOByQuestParticipantId(participantIds, contest);
        Multimap<Long, QuestSubmissionDTO> submissionMap = HashMultimap.create();
        for (QuestSubmissionDTO submission : submissions) {
            submissionMap.put(submission.getQuestParticipantId(), submission);
        }

        JsonArray arr = new JsonArray();
        for (QuestParticipant questParticipant : participants) {
            JsonObject o = new JsonObject();
            o.addProperty("id", questParticipant.getId());
            o.addProperty("name", questParticipant.getContestParticipant().getFullname());
            o.addProperty("points", questParticipant.getPoints());
//            o.addProperty("solved", questParticipant.getNumSolvedSubmissions());
            if (extended) {
                for (QuestSubmissionDTO submission : submissionMap.get(questParticipant.getId())) {
                    JsonObject s = new JsonObject();
                    s.addProperty("state", submission.getSubmissionState().toString());
                    s.addProperty("reason", submission.getReasonToReject());
                    o.add(String.valueOf(submission.getQuestChallengeId()), s);
                }
            }
            arr.add(o);
        }
        return arr;
    }

    public static void applyHashtagPrefix(String hashtagPrefix, final List<QuestChallenge> challenges) {
        if (challenges != null) {
            for (QuestChallenge challenge : challenges) {
                challenge.setHashtagPrefix(hashtagPrefix);
            }
        }
    }

}
