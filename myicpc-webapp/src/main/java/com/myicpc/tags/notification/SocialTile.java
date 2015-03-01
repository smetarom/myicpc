package com.myicpc.tags.notification;

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
    protected String getTitle() {
        Object[] params = new Object[2];
        if (isTemplate) {
            params[0] = "{{authorName}}";
            params[1] = "{{title}}";
        } else {
            params[0] = notification.getAuthorName();
            params[1] = notification.getTitle();
        }
        return String.format(getTitleFormat(), params);
    }

    protected String  getTitleFormat() {
        return "%s <small>%s</small>";
    }

    @Override
    protected void renderBody(JspWriter out) throws IOException, JspException {
        super.renderBody(out);
        renderMedia(out);
    }

    protected void renderMedia(JspWriter out) throws IOException, JspException {
        String imageFormat = "<img src=\"%s\" alt=\"\" class=\"img-responsive center-block\" />";
        String videoFormat = "<video src=\"%s\" controls muted loop width=\"100%%\">\n" +
                "  Your browser does not support the video player.\n" +
                "</video>";
        if (isTemplate) {
            out.print(String.format(imageFormat, "{{imageUrl}}"));
            out.print(String.format(videoFormat, "{{videoUrl}}"));
        } else {
            if (!StringUtils.isEmpty(notification.getVideoUrl())) {
                out.print(String.format(videoFormat, notification.getVideoUrl()));
            } else if (!StringUtils.isEmpty(notification.getImageUrl())) {
                out.print(String.format(imageFormat, notification.getImageUrl()));
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
