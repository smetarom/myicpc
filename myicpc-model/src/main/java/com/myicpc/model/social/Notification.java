package com.myicpc.model.social;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.myicpc.commons.utils.TimeUtils;
import com.myicpc.model.IdGeneratedContestObject;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.Map.Entry;

/**
 * Notification about system event or about external event
 *
 * @author Roman Smetana
 */
@Entity
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "Notification_id_seq")
public class Notification extends IdGeneratedContestObject {
    private static final Logger logger = LoggerFactory.getLogger(Notification.class);
    private static final long serialVersionUID = 7111388236875951578L;

    /**
     * Type of the notification
     *
     * @author Roman Smetana
     */
    public enum NotificationType {
        SCOREBOARD_SUCCESS("Scoreboard", "Scoreboard success", "submissionSuccess"),
        SCOREBOARD_FAILED("Scoreboard", "Scoreboard failed", "submissionFailed"),
        SCOREBOARD_SUBMIT("Scoreboard", "Scoreboard submitted", "submissionSubmitted"),
        TWITTER("Tweet", "Twitter", "t"),
        ARTICLE("Article", "Article", "a"),
        ADMIN_NOTIFICATION("Admin notification", "Admin notification", "an"),
        GALLERY("Gallery", "Gallery", "ga"),
        VINE("Vine", "Vine", "vi"),
        INSTAGRAM_VIDEO("Instagram", "Instagram video", "iv"),
        INSTAGRAM_IMAGE("Instagram", "Instagram photo", "im"),
        POLL_OPEN("Poll", "Poll open", "po"),
        POLL_CLOSE("Poll", "Poll close", "pc"),
        YOUTUBE_VIDEO("YouTube", "YouTube", "yt"),
        SCHEDULE_EVENT_OPEN("Schedule", "Schedule event open", "seo"),
        QUEST_VOTE_WINNER_TWITTER("Quest vote winner", "Quest vote winner", "qvwt"),
        QUEST_VOTE_WINNER_VINE("Quest vote winner", "Quest vote winner", "qvwv"),
        QUEST_VOTE_WINNER_INSTAGRAM_IMAGE("Quest vote winner", "Quest vote winner", "qvwim"),
        QUEST_VOTE_INSTAGRAM_VIDEO("Quest vote winner", "Quest vote winner", "qvwiv"),
        QUEST_CHALLENGE("Quest challenge", "Quest challenge", "qch"),
        ANALYST_MESSAGE("Analytics Message", "Analytics Message", "ame");
        /**
         * Human readable name of notification type
         */
        private String label;
        /**
         * Full description of notification type
         */
        private String description;
        /**
         * Short name for internal representation
         */
        private String code;

        private NotificationType(final String label, final String description, final String code) {
            this.label = label;
            this.description = description;
            this.code = code;
        }

        public String getLabel() {
            return label;
        }

        public String getDescription() {
            return description;
        }

        public String getCode() {
            return code;
        }

        public boolean isScoreboardSuccess() {
            return this == SCOREBOARD_SUCCESS;
        }

        public boolean isScoreboardFailed() {
            return this == SCOREBOARD_FAILED;
        }

        public boolean isScoreboardSubmitted() {
            return this == SCOREBOARD_SUBMIT;
        }

        public boolean isTwitter() {
            return this == TWITTER;
        }

        public boolean isArticle() {
            return this == ARTICLE;
        }

        public boolean isAdminNotification() {
            return this == ADMIN_NOTIFICATION;
        }

        public boolean isGallery() {
            return this == GALLERY;
        }

        public boolean isVine() {
            return this == VINE;
        }

        public boolean isInstagramVideo() {
            return this == INSTAGRAM_VIDEO;
        }

        public boolean isInstagramImage() {
            return this == INSTAGRAM_IMAGE;
        }

        public boolean isPollOpen() {
            return this == POLL_OPEN;
        }

        public boolean isPollClose() {
            return this == POLL_OPEN;
        }

        public boolean isYoutubeVideo() {
            return this == YOUTUBE_VIDEO;
        }

        public boolean isScheduleEventOpen() {
            return this == SCHEDULE_EVENT_OPEN;
        }

        public boolean isQuestChallenge() {
            return this == QUEST_CHALLENGE;
        }

        public boolean isAnalyticsMessage() {
            return this == ANALYST_MESSAGE;
        }

    }

    /**
     * Group of notification based on the origin of the notification
     *
     * @author Roman Smetana
     */
    public enum NotificationCategory {
        SCOREBOARD, SOCIAL, GALLERY, QUEST, SCHEDULE, ADMIN_NOTIFICATION;
    }

    /**
     * Short description of the notification
     */
    @Column(length = 1024)
    private String title;
    /**
     * Body of the notification
     */
    @Lob
    @Type(type = "org.hibernate.type.StringClobType")
    private String body;
    /**
     * URL related to notification
     */
    private String url;
    /**
     * Author display name
     */
    private String displayName;
    /**
     * Profile picture of the author
     */
    private String profilePictureUrl;
    /**
     * Any extra information needed in notification
     */
    @Lob
    @Type(type = "org.hibernate.type.StringClobType")
    private String code;
    /**
     * Timestamp of the notification creation
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
    /**
     * ID related to the event, which created this notification
     */
    private Long entityId;
    /**
     * Does the notification contain swear words?
     */
    private boolean offensive;
    /**
     * Can the notification be promoted to featured notifications
     */
    private boolean featuredEligible = true;
    @Transient
    private String highlightedBody;
    /**
     * {@link #code} can be represented by JSON, this stores key value pairs
     */
    @Transient
    private Map<String, String> parsedCode;

    /**
     * Type of the notification
     */
    @Enumerated(EnumType.STRING)
    @NotNull
    private NotificationType notificationType;
    /**
     * Group of the notification
     */
    @Enumerated(EnumType.STRING)
    private NotificationCategory notificationCategory;

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(final String body) {
        this.body = body;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(final String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        parsedCode = null;
        this.code = code;
    }

    public boolean isOffensive() {
        return offensive;
    }

    public void setOffensive(final boolean offensive) {
        this.offensive = offensive;
    }

    public boolean isFeaturedEligible() {
        return featuredEligible;
    }

    public void setFeaturedEligible(final boolean featuredEligible) {
        this.featuredEligible = featuredEligible;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(final NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public NotificationCategory getNotificationCategory() {
        return notificationCategory;
    }

    public void setNotificationCategory(final NotificationCategory notificationCategory) {
        this.notificationCategory = notificationCategory;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(final Long teamId) {
        this.entityId = teamId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return timestamp in format HH:mm
     */
    @Transient
    public String getTime() {
        return TimeUtils.getTimeFormat().format(timestamp);
    }

    /**
     * @return timestamp in local time
     */
    @Transient
    public Date getLocalTimestamp() {
        return TimeUtils.convertUTCDateToLocal(getTimestamp(), contest.getContestSettings().getTimeDifference());
    }

    /**
     * Parses JSON into Map
     *
     * @return JSON object represented as map
     */
    public Map<String, String> getParsedCode() {
        if (parsedCode == null && code != null) {
            parsedCode = new HashMap<String, String>();
            JsonObject obj = new JsonParser().parse(code).getAsJsonObject();
            for (Entry<String, JsonElement> entry : obj.entrySet()) {
                parsedCode.put(entry.getKey(), entry.getValue().getAsString());
            }
        }
        return parsedCode;
    }

    /**
     * Returns list of poll options in the map where the key is "id", which is
     * internal, and value "choice", which represents the human readable label
     * of the option
     *
     * @return get poll choices, if the notification was created from poll
     */
    //TODO refactor
    @Transient
    public List<Entry<Integer, String>> getPollChoices() {
        String choices = getParsedCode().get("choices");
        if (StringUtils.isEmpty(choices)) {
            return null;
        }
        List<Entry<Integer, String>> list = new ArrayList<Map.Entry<Integer, String>>();
        JsonArray arr = new JsonParser().parse(choices).getAsJsonArray();
        for (JsonElement e : arr) {
            JsonObject o = e.getAsJsonObject();
            if (o.has("id") && o.has("choice")) {
                try {
                    list.add(new AbstractMap.SimpleEntry<Integer, String>(o.get("id").getAsInt(), o.get("choice").getAsString()));
                } catch (ClassCastException ex) {
                    logger.warn("Invalid option: " + o.toString(), ex);
                }
            }
        }
        return list;
    }

    public String getHighlightedBody() {
        return highlightedBody;
    }

    public void setHighlightedBody(String highlightedBody) {
        this.highlightedBody = highlightedBody;
    }
}
