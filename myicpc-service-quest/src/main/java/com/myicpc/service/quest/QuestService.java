package com.myicpc.service.quest;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myicpc.enums.NotificationType;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.quest.QuestChallenge;
import com.myicpc.model.quest.QuestParticipant;
import com.myicpc.model.quest.QuestSubmission;
import com.myicpc.model.social.Notification;
import com.myicpc.repository.social.NotificationRepository;
import com.myicpc.service.utils.lists.NotificationList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Roman Smetana
 */
@Service
public class QuestService {
    public static final int POSTS_PER_PAGE = 30;

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

    public JsonArray getJSONParticipants(final List<QuestParticipant> questParticipants, boolean extended) {
        JsonArray arr = new JsonArray();
        for (QuestParticipant questParticipant : questParticipants) {
            JsonObject o = new JsonObject();
            o.addProperty("id", questParticipant.getId());
            o.addProperty("name", questParticipant.getContestParticipant().getFullname());
            o.addProperty("points", questParticipant.getPoints());
            o.addProperty("solved", questParticipant.getNumSolvedSubmissions());
            if (extended) {
                for (QuestSubmission submission : questParticipant.getSubmissions()) {
                    JsonObject s = new JsonObject();
                    s.addProperty("state", submission.getSubmissionState().toString());
                    s.addProperty("reason", submission.getReasonToReject());
                    o.add(submission.getChallenge().getId().toString(), s);
                }
            }
            arr.add(o);
        }
        return arr;
    }

}
