package com.myicpc.master.service.quest;

import com.myicpc.enums.NotificationType;
import com.myicpc.master.dao.QuestDao;
import com.myicpc.master.service.social.GeneralSocialService;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.quest.QuestChallenge;
import com.myicpc.model.social.Notification;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Date;
import java.util.List;

/**
 * @author Roman Smetana
 */
@Stateless
public class QuestService extends GeneralQuestService {
    @Inject
    private QuestDao questDao;

    public void processNewOpenQuestChallenges(final Contest contest) {
        List<QuestChallenge> newChallenges = questDao.findAllNonpublishedStartedChallenges(contest);
        for (QuestChallenge newChallenge : newChallenges) {
            Notification notification = createNotification(newChallenge);
            sendQuestNotification(notification);

            newChallenge.setPublished(true);
            questDao.save(newChallenge);
        }
    }

    public Notification createNotification(final QuestChallenge questChallenge) {
        Notification notification = new Notification();
        notification.setTitle(questChallenge.getName());
        notification.setBody(questChallenge.getNotificationDescription());
        notification.setNotificationType(NotificationType.QUEST_CHALLENGE);
        notification.setImageUrl(questChallenge.getImageURL());
        notification.setEntityId(questChallenge.getId());
        notification.setTimestamp(new Date());
        notification.setContest(questChallenge.getContest());
        return notification;
    }
}
