package com.myicpc.tags.notification;

import com.myicpc.enums.NotificationType;
import com.myicpc.model.social.Notification;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.Locale;

/**
 * @author Roman Smetana
 */
public class NotificationTag extends SimpleTagSupport {
    private Notification notification;
    private NotificationType type;
    private Locale locale;

    public NotificationTag() {
        if (getJspContext() != null && getJspContext().getELContext() != null) {
            locale = getJspContext().getELContext().getLocale();
        }
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public void setType(String type) {
        this.type = NotificationType.valueOf(type);
    }

    @Override
    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();
        boolean isTemplate = true;

        NotificationType notificationType = null;
        if (notification != null) {
            notificationType = notification.getNotificationType();
            isTemplate = false;
        } else if (type != null) {
            notificationType = type;
        }
        if (notificationType == null) {
            throw new JspException("Notification type cannot be null.");
        }
        NotificationTile tile = getNotificationTile(notificationType, isTemplate);
        if (tile != null) {
            tile.render(out);
        }
    }

    protected NotificationTile getNotificationTile(final NotificationType notificationType, boolean isTemplate) {
        PageContext pageContext = (PageContext) getJspContext();
        NotificationTile tile = null;
        // submission notification
        if (notificationType.isScoreboardSuccess() ||
                notificationType.isScoreboardFailed() ||
                notificationType.isScoreboardSubmitted() ||
                notificationType.isTeamAnalyticsMessage() ||
                notificationType.isAnalyticsMessage()) {
            tile = new SubmissionTile(notification, isTemplate, locale, pageContext);
        } else if (notificationType.isTwitter()) {
            tile = new TwitterTile(notification, isTemplate, locale, pageContext);
        } else if (notificationType.isInstagram()) {
            tile = new InstagramTile(notification, isTemplate, locale, pageContext);
        } else if (notificationType.isVine()) {
            tile = new VineTile(notification, isTemplate, locale, pageContext);
        } else if (notificationType.isPicasa()) {
            tile = new PicasaTile(notification, isTemplate, locale, pageContext);
        } else if (notificationType.isOfficialGallery()) {
            tile = new OfficialGalleryTile(notification, isTemplate, locale, pageContext);
        } else if (notificationType.isQuestChallenge()) {
            tile = new QuestChallengeTile(notification, isTemplate, locale, pageContext);
        } else if (notificationType.isAdminNotification()) {
            tile = new AdminNotificationTile(notification, isTemplate, locale, pageContext);
        } else if (notificationType.isPollOpen()) {
            tile = new PollTile(notification, isTemplate, locale, pageContext);
        } else if (notificationType.isScheduleEventOpen()) {
            tile = new EventOpenTile(notification, isTemplate, locale, pageContext);
        }
        // TODO more notification types to come
        return tile;
    }
}
