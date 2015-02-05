package com.myicpc.tags.notification;

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
    private Notification.NotificationType type;
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
        this.type = Notification.NotificationType.valueOf(type);
    }

    @Override
    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();
        boolean isTemplate = true;

        Notification.NotificationType notificationType = null;
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

    protected NotificationTile getNotificationTile(final Notification.NotificationType notificationType, boolean isTemplate) {
        PageContext pageContext = (PageContext) getJspContext();
        NotificationTile tile = null;
        // submission notification
        if (notificationType.isScoreboardSuccess() ||
                notificationType.isScoreboardFailed() ||
                notificationType.isScoreboardSubmitted() ||
                notificationType.isAnalyticsMessage()) {
            tile = new SubmissionTile(notification, isTemplate, locale, pageContext);
        }
        // TODO more notification types to come
        return tile;
    }
}
