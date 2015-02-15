package com.myicpc.tags.notification;

import com.myicpc.commons.utils.MessageUtils;
import com.myicpc.model.social.Notification;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import java.io.IOException;
import java.util.Locale;

/**
 * @author Roman Smetana
 */
public abstract class SocialTile extends NotificationTile {

    public SocialTile(Notification notification, boolean isTemplate, Locale locale, PageContext pageContext) {
        super(notification, isTemplate, locale, pageContext);
    }

    @Override
    protected void renderBody(JspWriter out) throws IOException, JspException {
        super.renderBody(out);
        renderImage(out);
    }

    protected void renderImage(JspWriter out) throws IOException, JspException {
        if (isTemplate) {
            out.print(String.format("<img src=\"%s\" alt=\"\" class=\"img-responsive\" />", "{{imageUrl}}"));
        } else {
            if (!StringUtils.isEmpty(notification.getImageUrl())) {
                out.print(String.format("<img src=\"%s\" alt=\"\" class=\"img-responsive\" />", notification.getImageUrl()));
            }
        }
    }

    @Override
    protected void renderAvatar(JspWriter out) throws IOException, JspException {
        if (isTemplate) {
            renderAvatarImage(out, "{{profileUrl}}", "{{authorName}}");
        } else {
            renderAvatarImage(out, notification.getProfilePictureUrl(), notification.getAuthorName());
        }
    }

}
