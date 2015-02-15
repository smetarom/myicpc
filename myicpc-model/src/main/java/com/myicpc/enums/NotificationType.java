package com.myicpc.enums;

/**
 * Type of the notification
 *
 * @author Roman Smetana
 */
public enum NotificationType {
    SCOREBOARD_SUCCESS("Scoreboard", "Scoreboard success", "submissionSuccess"),
    SCOREBOARD_FAILED("Scoreboard", "Scoreboard failed", "submissionFailed"),
    SCOREBOARD_SUBMIT("Scoreboard", "Scoreboard submitted", "submissionSubmitted"),
    TWITTER("Tweet", "Twitter", "twitter"),
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
