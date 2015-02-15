package com.myicpc.service.notification;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myicpc.commons.utils.TimeUtils;
import com.myicpc.commons.utils.WikiUtils;
import com.myicpc.enums.GalleryMediaType;
import com.myicpc.enums.NotificationType;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Team;
import com.myicpc.model.eventFeed.TeamProblem;
import com.myicpc.model.poll.Poll;
import com.myicpc.model.quest.QuestChallenge;
import com.myicpc.model.quest.QuestSubmission;
import com.myicpc.model.rss.RSSMessage;
import com.myicpc.model.schedule.Event;
import com.myicpc.model.social.AdminNotification;
import com.myicpc.model.social.GalleryMedia;
import com.myicpc.model.social.Notification;
import com.myicpc.model.social.TwitterMessage;
import com.myicpc.model.teamInfo.TeamInfo;
import com.myicpc.repository.social.NotificationRepository;
import com.myicpc.service.analyst.MessageAnalystService;
import com.myicpc.service.dto.AnalystMessageDTO;
import com.myicpc.service.publish.PublishService;
import com.myicpc.service.utils.lists.NotificationList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import twitter4j.Status;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides services to manage {@link Notification}
 *
 * @author Roman Smetana
 */
@Service("notificationService")
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private List<NotificationType> hiddenNotificationTypesOnTimeline = new ArrayList<>();

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private MessageAnalystService messageAnalystService;

    @Autowired
    private PublishService publishService;

    /**
     * Construct default list of notification types, which should be ignored by
     * default
     */
    public NotificationServiceImpl() {
        hiddenNotificationTypesOnTimeline = NotificationList.newList().addScoreboardSubmitted().addScoreboardFailed().addPollOpen()
                .addQuestWinnerTwitter().addQuestWinnerVine();
    }

    /**
     * Returns JSON representation of {@link Notification}
     *
     * @param notification
     * @return JSON representation
     */
    public static JsonObject getNotificationInJson(final Notification notification, final Contest contest) {
        DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.ENGLISH);
        JsonObject notificationObject = new JsonObject();
        notificationObject.addProperty("id", notification.getId());
        notificationObject.addProperty("type", notification.getNotificationType().getCode());
        notificationObject.addProperty("title", notification.getTitle());
        notificationObject.addProperty("body", notification.getBody());
        notificationObject.addProperty("user", notification.getAuthorName());
        notificationObject.addProperty("url", notification.getUrl());
        notificationObject.addProperty("entityId", notification.getEntityId());
        notificationObject.addProperty("code", notification.getCode());
        Date timestamp = notification.getLocalTimestamp();
        notificationObject.addProperty("timestamp", timestamp != null ? formatter.format(timestamp) : "");
        notificationObject.addProperty("profileUrl", notification.getProfilePictureUrl());

        return notificationObject;
    }

    @Override
    public void onPendingSubmissionNotification(final TeamProblem teamProblem) {
        Notification notification = createNotification(teamProblem, NotificationType.SCOREBOARD_SUBMIT);
        publishService.broadcastNotification(notification, teamProblem.getTeam().getContest());
    }

    @Override
    public void onSuccessSubmissionNotification(final TeamProblem teamProblem) {
        Notification notification = createNotification(teamProblem, NotificationType.SCOREBOARD_SUCCESS);
        publishService.broadcastNotification(notification, teamProblem.getTeam().getContest());
    }

    @Override
    public void onFailedSubmissionNotification(TeamProblem teamProblem) {
        // TODO
    }

    @Override
    public void onSubmission(TeamProblem teamProblem, List<Team> effectedTeams) {
        // ignored
    }

    protected Notification createNotification(final TeamProblem teamProblem, final NotificationType notificationType) {
        NotificationBuilder builder = new NotificationBuilder(teamProblem);
        builder.setTitle(messageAnalystService.getAnalystTitle(teamProblem));
        builder.setBody(messageAnalystService.getAnalystMessage(teamProblem));
        builder.setContest(teamProblem.getTeam().getContest());
        builder.setNotificationType(notificationType);
        builder.setUrl("/team/" + teamProblem.getTeam().getId());
        String hashtags = "";
        TeamInfo teamInfo = teamProblem.getTeam().getTeamInfo();
        if (teamInfo != null && teamInfo.getHashtag() != null && !teamInfo.getHashtag().isEmpty()) {
            hashtags = teamInfo.getHashtag() + ",";
        }
        hashtags += teamProblem.getTeam().getContest().getHashtag();
        builder.setCode("{\"hashtags\":\"" + hashtags + "\"}");
        return notificationRepository.save(builder.build());
    }

    public Notification createNotification(final Event event) {
        NotificationBuilder builder = new NotificationBuilder(event);
        builder.setTitle(event.getName());
        // TODO renew event html
//		builder.setBody(FormatUtils.formatEvent(event));
        builder.setNotificationType(NotificationType.SCHEDULE_EVENT_OPEN);
        builder.setUrl(event.getCode());

        return notificationRepository.save(builder.build());
    }

    public Notification createNotification(final Poll poll, final NotificationType notificationType) {
        NotificationBuilder builder = new NotificationBuilder(poll, notificationType, notificationRepository);
        builder.setTitle(poll.getQuestion());
        builder.setBody(poll.getDescription());
        builder.setNotificationType(notificationType);
        // TODO refactor?
        builder.setCode(getPollJSON(poll).toString());

        return notificationRepository.save(builder.build());
    }

    public Notification createNotification(final AnalystMessageDTO analystMessageDTO) {
        NotificationBuilder builder = new NotificationBuilder();
        builder.setTitle(analystMessageDTO.getTitle());
        builder.setBody(analystMessageDTO.getMessage());
        builder.setNotificationType(NotificationType.ANALYST_MESSAGE);

        return notificationRepository.save(builder.build());
    }

    @Override
    public Notification createNotification(final Status twitterStatus, final Contest contest) {
        NotificationBuilder builder = new NotificationBuilder();
        builder.setTitle(twitterStatus.getUser().getScreenName());
        builder.setBody(parseTweetText(twitterStatus));
        builder.setHashTags(getHashtagsFromTweet(twitterStatus.getText()));
        builder.setNotificationType(NotificationType.TWITTER);
        builder.setExternalId(String.valueOf(twitterStatus.getId()));
        if (twitterStatus.getRetweetedStatus() != null) {
            builder.setRetweetedId(twitterStatus.getRetweetedStatus().getId());
        }
        builder.setAuthorName(twitterStatus.getUser().getName());
        builder.setProfilePictureUrl(twitterStatus.getUser().getProfileImageURL());
        builder.setTimestamp(twitterStatus.getCreatedAt());
        builder.setContest(contest);

        if (!twitterStatus.isRetweet()) {
            if (twitterStatus.getMediaEntities() != null && twitterStatus.getMediaEntities().length > 0) {
                builder.setImageUrl(twitterStatus.getMediaEntities()[0].getMediaURL());
            }
        }

        return notificationRepository.save(builder.build());
    }

    public Notification createNotification(final TwitterMessage twitterMessage) {
        NotificationBuilder builder = new NotificationBuilder(twitterMessage);
        builder.setTitle(twitterMessage.getUsername());
        builder.setBody(twitterMessage.getDescription());
        builder.setNotificationType(NotificationType.TWITTER);
        JsonObject codeObject = new JsonObject();
        codeObject.addProperty("tweetId", twitterMessage.getTweetId() + "");
        if (twitterMessage.getMediaURL() != null) {
            codeObject.addProperty("mediaUrl", twitterMessage.getMediaURL() + "");
        }
        codeObject.addProperty("fullname", twitterMessage.getUserFullName());
        builder.setCode(codeObject.toString());
        builder.setAuthorName(twitterMessage.getUserFullName());
        builder.setTimestamp(twitterMessage.getTimestamp());
        builder.setProfilePictureUrl(twitterMessage.getProfileImageUrl());
        builder.setOffensive();

        return notificationRepository.save(builder.build());
    }

    public Notification createNotification(final RSSMessage rssMessage) {
        NotificationBuilder builder = new NotificationBuilder(rssMessage);
        builder.setTitle(rssMessage.getName());
        builder.setBody(rssMessage.getText());
        builder.setNotificationType(NotificationType.ARTICLE);
        builder.setUrl(rssMessage.getUrl());
        builder.setCode("{\"feed\": \"" + rssMessage.getRssFeed().getName() + "\"}");
        builder.setContest(rssMessage.getRssFeed().getContest());
        builder.setOffensive();

        return notificationRepository.save(builder.build());
    }

    public Notification createNotification(final QuestChallenge questChallenge) {
        NotificationBuilder builder = new NotificationBuilder(questChallenge, NotificationType.QUEST_CHALLENGE, notificationRepository);
        builder.setTitle(questChallenge.getName());
        builder.setBody(questChallenge.getNotificationDescription());
        builder.setNotificationType(NotificationType.QUEST_CHALLENGE);
        builder.setUrl(questChallenge.getImageURL());
        builder.setCode("{\"hashtag\":\"" + questChallenge.getHashtag() + "\"}");

        return notificationRepository.save(builder.build());
    }

    public Notification createNotification(final GalleryMedia galleryMedia) {
        NotificationBuilder builder = new NotificationBuilder(galleryMedia);
        builder.setTitle(galleryMedia.getUsername());
        builder.setBody(galleryMedia.getDescription());
        builder.setTimestamp(galleryMedia.getTimestamp() == null ? new Date() : galleryMedia.getTimestamp());
        builder.setUrl(galleryMedia.getMediaURL());
        builder.setNotificationType(GalleryMediaType.toNotificationType(galleryMedia.getGalleryMediaType()));
        builder.setAuthorName(galleryMedia.getDisplayName());
        builder.setProfilePictureUrl(galleryMedia.getProfilePhotoURL());
        builder.setCode("{\"thumbnailUrl\": \"" + galleryMedia.getThumbnailURL() + "\", \"additionalUrl\": \"" + galleryMedia.getAdditionalURL()
                + "\", \"mediaId\": \"" + galleryMedia.getMediaId() + "\"}");
        builder.setOffensive();

        return notificationRepository.save(builder.build());
    }

    public Notification createNotification(final AdminNotification adminNotification) {
        NotificationBuilder builder = new NotificationBuilder(adminNotification, NotificationType.QUEST_CHALLENGE, notificationRepository);
        builder.setTitle(WikiUtils.parseWikiSyntax(adminNotification.getTitle()));
        builder.setBody(WikiUtils.parseWikiSyntax(adminNotification.getBody()));
        builder.setNotificationType(NotificationType.ADMIN_NOTIFICATION);

        return notificationRepository.save(builder.build());
    }


    /**
     * Broadcast a notification about Quest voting round winner
     *
     * @param submission winning Quest submission in voting round
     * @return notification for winning Quest submission
     */
    @Transactional
    @Deprecated
    public Notification notificationForQuestVoteWinner(final QuestSubmission submission) {

        // TODO what to do here?
        Notification notification = new Notification();
        notification.setTitle(submission.getParticipant().getContestParticipant().getTwitterUsername());
        notification.setBody(submission.getEscapedText());
        notification.setNotificationType(submission.getSubmissionType().getNotificationType());
        notification.setUrl(submission.getMediaURL());
        notification.setProfilePictureUrl(submission.getParticipant().getContestParticipant().getProfilePictureUrl());
        notification.setTimestamp(new Date());

        notification = notificationRepository.save(notification);

        return notification;
    }

    /**
     * Broadcast a notification for opening Quest challenge
     *
     * @param challenge opened Quest challenge
     * @return notification for challenge
     */
    @Transactional
    @Deprecated
    public Notification notificationForQuestChallenge(final QuestChallenge challenge) {
        Notification notification = getCurrentNotification(challenge.getId(), NotificationType.QUEST_CHALLENGE);
        notification.setTitle(challenge.getName());
        notification.setBody(challenge.getNotificationDescription());
        notification.setNotificationType(NotificationType.QUEST_CHALLENGE);
        notification.setEntityId(challenge.getId());
        notification.setUrl(challenge.getImageURL());
        notification.setTimestamp(new Date());
        notification.setCode("{\"hashtag\":\"" + challenge.getHashtag() + "\"}");

        notification = notificationRepository.save(notification);

        return notification;
    }

    /**
     * Returns up to the limit of the latest notifications
     *
     * @param limit number of returned notifications
     * @return latest notification
     */
    public List<Notification> getLatestNotifications(final int limit, final List<NotificationType> notificationTypes, Contest contest) {
        Pageable pageable = new PageRequest(0, limit);
        Page<Notification> page = notificationRepository.findByNotificationTypesOrderByIdDesc(notificationTypes, contest, pageable);
        return page.getContent();
    }

    /**
     * Returns up to the limit of the latest notifications except default
     * ignored notification types
     *
     * @param limit number of returned notifications
     * @return latest notification
     */
    public List<Notification> getLatestNotificationsExceptDefaultList(final int limit) {
        return getLatestNotificationsExceptList(limit, hiddenNotificationTypesOnTimeline);
    }

    /**
     * Returns up to the limit of the latest notifications except given ignored
     * notification types
     *
     * @param limit       number of returned notifications
     * @param exceptTypes ignored notification types
     * @return latest notification
     */
    public List<Notification> getLatestNotificationsExceptList(final int limit, final List<NotificationType> exceptTypes) {
        Pageable pageable = new PageRequest(0, limit, Direction.DESC, "timestamp");
        Page<Notification> page = notificationRepository.findAllExceptListOrderByIdDesc(exceptTypes, pageable);

        return page.getContent();
    }

    /**
     * Returns up to the limit of the latest notifications except default
     * ignored notification types since last notification ID
     *
     * @param lastTimestamp notification timestamp
     * @param limit         number of returned notifications
     * @return latest notification
     */
    public List<Notification> getLatestNotificationsExceptSubmissions(final Long lastTimestamp, final int limit) {
        return getLatestNotificationsExceptSubmissions(lastTimestamp, limit, hiddenNotificationTypesOnTimeline);
    }

    /**
     * Returns up to the limit of the latest notifications except default
     * ignored notification types since last notification ID
     *
     * @param lastTimestamp notification timestamp
     * @param limit         number of returned notifications
     * @param exceptTypes   ignored notification types
     * @return latest notification
     */
    public List<Notification> getLatestNotificationsExceptSubmissions(final Long lastTimestamp, final int limit,
                                                                      final List<NotificationType> exceptTypes) {
        Pageable pageable = new PageRequest(0, limit, Direction.DESC, "timestamp");
        Date timestamp = new Date(lastTimestamp);
        Page<Notification> page = notificationRepository.findAllExceptListOrderByIdDesc(timestamp, hiddenNotificationTypesOnTimeline, pageable);

        return page.getContent();
    }

    public List<Notification> getScoreboardNotifications(final Contest contest) {
        List<NotificationType> notificationTypes = Lists.newArrayList(NotificationType.SCOREBOARD_SUBMIT, NotificationType.SCOREBOARD_SUCCESS);
        return getLatestNotifications(10, notificationTypes, contest);
    }

    /**
     * Return list of featured notification, which are presented to user as
     * important on the top of the page
     *
     * @param ignoredFeatured ids of notifications ignored by user
     * @return list of featured notifications
     */
    @Override
    public List<Notification> getFeaturedNotifications(final List<Long> ignoredFeatured) {
        List<Notification> notifications = new ArrayList<Notification>();
        notifications.addAll(notificationRepository.findCurrentPollNotifications(new Date(), ignoredFeatured));
        notifications.addAll(notificationRepository.findCurrentAdminNotifications(new Date(), ignoredFeatured));
        notifications.addAll(notificationRepository.findCurrentQuestChallengeNotifications(new Date(), ignoredFeatured));

        Collections.sort(notifications, new Comparator<Notification>() {

            @Override
            public int compare(final Notification n1, final Notification n2) {
                if (n1.getTimestamp() == null) {
                    return -1;
                }
                if (n2.getTimestamp() == null) {
                    return 1;
                }
                return -1 * n1.getTimestamp().compareTo(n2.getTimestamp());
            }
        });

        return notifications;
    }

    /**
     * Returns JSON representation of list of notifications
     *
     * @param notifications list of notifications
     * @return JSON representation of list of notifications
     */
    public static JsonArray getNotificationsInJson(final List<Notification> notifications) {
        JsonArray arr = new JsonArray();
        if (notifications != null) {
            for (Notification notification : notifications) {
                arr.add(getNotificationInJson(notification));
            }
        }
        return arr;
    }

    /**
     * Returns JSON representation of {@link Notification}
     *
     * @param notification
     * @return JSON representation
     */
    public static JsonObject getNotificationInJson(final Notification notification) {
        DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.ENGLISH);
        JsonObject notificationObject = new JsonObject();
        notificationObject.addProperty("id", notification.getId());
        notificationObject.addProperty("externalId", notification.getExternalId());
        notificationObject.addProperty("type", notification.getNotificationType().getCode());
        notificationObject.addProperty("title", notification.getTitle());
        notificationObject.addProperty("body", notification.getBody());
        notificationObject.addProperty("authorName", notification.getAuthorName());
        notificationObject.addProperty("url", notification.getUrl());
        notificationObject.addProperty("imageUrl", notification.getImageUrl());
        notificationObject.addProperty("videoUrl", notification.getVideoUrl());
        notificationObject.addProperty("entityId", notification.getEntityId());
        notificationObject.addProperty("code", notification.getCode());
        Date timestamp = TimeUtils.convertUTCDateToLocal(notification.getTimestamp(), notification.getContest().getContestSettings().getTimeDifference());
        notificationObject.addProperty("timestamp", timestamp != null ? formatter.format(timestamp) : "");
        notificationObject.addProperty("profileUrl", notification.getProfilePictureUrl());

        return notificationObject;
    }

    /**
     * Delete notifications of type for entities
     *
     * @param entityIds
     * @param notificationType
     */
    public void deleteNoficicationsForEntity(List<Long> entityIds, NotificationType notificationType) {
        for (Long id : entityIds) {
            deleteNoficicationsForEntity(id, notificationType);
        }
    }

    /**
     * Delete notifications of type for entity
     *
     * @param entityId
     * @param notificationType
     */
    public void deleteNoficicationsForEntity(Long entityId, NotificationType notificationType) {
        List<Notification> notifications = notificationRepository.findByEntityIdAndNotificationType(entityId, notificationType);
        notificationRepository.delete(notifications);
    }

    /**
     * Get existing notification or return new, if notification does not exist
     *
     * @param entityId
     * @param notificationType
     * @return existing notification or new, if notification does not exist
     */
    private Notification getCurrentNotification(Long entityId, NotificationType notificationType) {
        List<Notification> notifications = notificationRepository.findByEntityIdAndNotificationType(entityId, notificationType);
        if (notifications.size() == 1) {
            return notifications.get(0);
        }
        return new Notification();
    }

    /**
     * Returns JSON representation of poll
     *
     * @param poll poll
     * @return JSON representation
     */
    private JsonObject getPollJSON(final Poll poll) {
        JsonObject pollObject = new JsonObject();
        pollObject.addProperty("id", poll.getId());
        pollObject.addProperty("question", poll.getQuestion());
        pollObject.addProperty("pollType", poll.getPollType().toString());
        // TODO
        // pollObject.addProperty("choices", poll.getChoices());

        return pollObject;
    }

    /**
     * Finds Twitter hashtags, usernames, and URLs in the tweet
     *
     * @param status
     *            Twitter tweet
     * @return tweet message enhanced by HTML tags
     */
    public static String parseTweetText(final Status status) {
        String text = null;
        if (status.isRetweet() && status.getRetweetedStatus() != null) {
            text = "RT @" + status.getRetweetedStatus().getUser().getScreenName() + ": " + status.getRetweetedStatus().getText();
        } else {
            text = status.getText();
        }
        text = text.replaceAll("((https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|])", "<a href='$1'>$1</a>");
        text = text.replaceAll("@([\\p{L}+0-9-_]+)", "<a href='http://twitter.com/$1'>@$1</a>");
        text = text.replaceAll("#([\\p{L}+0-9-_]+)", "<a href='https://twitter.com/search/?src=hash&amp;q=%23$1'>#$1</a>");
        text = text.replaceAll("([\\ud800-\\udbff\\udc00-\\udfff])", "");
        return text;
    }

    /**
     * Gets all hashtags from the tweet body separated by |
     *
     * @param tweet
     *            tweet body
     * @return hashtags separated by |
     */
    public static String getHashtagsFromTweet(final String tweet) {
        Pattern pattern = Pattern.compile("(#[\\p{L}+0-9-_]+)");
        Matcher matcher = pattern.matcher(tweet);
        StringBuilder hashtags = new StringBuilder("|");
        while (matcher.find()) {
            hashtags.append(matcher.group().substring(1)).append("|");
        }
        return hashtags.toString();
    }


}
