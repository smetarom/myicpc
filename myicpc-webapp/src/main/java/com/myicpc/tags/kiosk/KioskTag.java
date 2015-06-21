package com.myicpc.tags.kiosk;

import com.myicpc.enums.NotificationType;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.Locale;

/**
 * @author Roman Smetana
 */
public class KioskTag extends SimpleTagSupport {
    private NotificationType notificationType;
    private Locale locale;

    public KioskTag() {
        if (getJspContext() != null && getJspContext().getELContext() != null) {
            locale = getJspContext().getELContext().getLocale();
        }
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    @Override
    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();
        if (notificationType == null) {
            throw new JspException("Notification type cannot be null.");
        }
        KioskTile tile = getNotificationTile(notificationType);
        if (tile != null) {
            tile.render(out);
        }
    }

    private KioskTile getNotificationTile(NotificationType notificationType) {
        PageContext pageContext = (PageContext) getJspContext();
        switch (notificationType) {
            case TWITTER:
                return new TwitterKioskTile(locale, pageContext);
            case INSTAGRAM:
                return new InstagramKioskTile(locale, pageContext);
            case VINE:
                return new VineKioskTile(locale, pageContext);
            case QUEST_CHALLENGE:
                return new QuestChallengeKioskTile(locale, pageContext);
            case PICASA:
                return new PicasaKioskTile(locale, pageContext);
            case YOUTUBE_VIDEO:
                return new YoutubeKioskTile(locale, pageContext);
            case ADMIN_NOTIFICATION:
                return new AdminNotificationKioskTile(locale, pageContext);
            case POLL_OPEN:
                return new PollOpenNotificationKioskTile(locale, pageContext);
            case SCHEDULE_EVENT_OPEN:
                return new ScheduleEventNotificationKioskTile(locale, pageContext);
        }
        return null;
    }
}
