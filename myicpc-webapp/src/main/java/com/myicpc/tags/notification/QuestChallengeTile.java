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
public class QuestChallengeTile extends NotificationTile {

    public QuestChallengeTile(Notification notification, boolean isTemplate, boolean editable, Locale locale, PageContext pageContext) {
        super(notification, isTemplate, editable, locale, pageContext);
    }

    @Override
    protected void renderAvatar(JspWriter out) throws IOException, JspException {
        renderAvatarIcon(out, "fa fa-trophy");
    }

    @Override
    protected void renderFooterTitle(JspWriter out) throws IOException, JspException {
        out.print(String.format("<span class=\"fa fa fa-trophy\"></span> %s", MessageUtils.getMessage("quest", locale)));
    }

    @Override
    protected void renderBody(JspWriter out) throws IOException, JspException {
        super.renderBody(out);
        renderMedia(out);
    }
}
