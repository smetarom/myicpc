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
public class SubmissionTile extends NotificationTile {

    public SubmissionTile(Notification notification, boolean isTemplate, boolean editable, Locale locale, PageContext pageContext) {
        super(notification, isTemplate, editable, locale, pageContext);
    }

    @Override
    protected void renderAvatar(JspWriter out) throws IOException, JspException {
        renderAvatarImage(out, "/images/logos/icpc_analytics_square_logo_small.png", "ICPC analytics");
    }

    @Override
    protected void renderFooterTitle(JspWriter out) throws IOException, JspException {
        out.print(String.format(" <a href=\"%s\" target=\"_blank\"><span class=\"glyphicon glyphicon-log-out\"></span> %s</a> ", resolveUrl("/credits/analytics"), MessageUtils.getMessage("credits.analytics.short", locale)));
    }
}
