package com.myicpc.tags.notification;

import com.myicpc.commons.utils.MessageUtils;
import com.myicpc.model.social.Notification;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import java.io.IOException;
import java.util.Locale;

/**
 * @author Roman Smetana
 */
public class AdminNotificationTile extends NotificationTile {

    public AdminNotificationTile(Notification notification, boolean isTemplate, boolean editable, Locale locale, PageContext pageContext) {
        super(notification, isTemplate, editable, locale, pageContext);
    }

    @Override
    protected void renderAvatar(JspWriter out) throws IOException, JspException {
        renderAvatarIcon(out, "fa fa-exclamation-triangle");
    }

    @Override
    protected void renderFooterTitle(JspWriter out) throws IOException, JspException {
        out.print(String.format("<span class=\"fa fa-exclamation-triangle\"></span> %s", MessageUtils.getMessage("adminNotification", locale)));
    }

    @Override
    protected void renderBody(JspWriter out) throws IOException, JspException {
        super.renderBody(out);
        renderMedia(out);
    }
}
