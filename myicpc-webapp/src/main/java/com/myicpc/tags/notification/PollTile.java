package com.myicpc.tags.notification;

import com.myicpc.commons.utils.MessageUtils;
import com.myicpc.controller.functions.JSPCustomFunctions;
import com.myicpc.model.social.Notification;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import java.io.IOException;
import java.util.Locale;

/**
 * @author Roman Smetana
 */
public class PollTile extends NotificationTile {

    public PollTile(Notification notification, boolean isTemplate, Locale locale, PageContext pageContext) {
        super(notification, isTemplate, locale, pageContext);
    }

    @Override
    protected void renderAvatar(JspWriter out) throws IOException, JspException {
        renderAvatarIcon(out, "glyphicon glyphicon-bullhorn");
    }

    @Override
    protected String getBody() {
        StringBuilder html = new StringBuilder();
        html.append("<div class=\"poll-tile\">");
        if (isTemplate) {
            // TODO
            html.append(super.getBody());
        } else {
            html.append(JSPCustomFunctions.pollOptionsToSelect(notification.getBody()));
        }
        html.append("</div>");
        return html.toString();
    }

    @Override
    protected void renderFooterTitle(JspWriter out) throws IOException, JspException {
        out.print(String.format("<span class=\"glyphicon glyphicon-bullhorn\"></span> %s", MessageUtils.getMessage("timeline.tile.polls", locale)));
    }
}
