package com.myicpc.tags.notification;

import com.myicpc.commons.utils.MessageUtils;
import com.myicpc.model.social.Notification;
import com.myicpc.tags.utils.HandlebarsUtils;
import com.myicpc.tags.utils.JSPUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Locale;

import static com.myicpc.tags.utils.TagConstants.IMAGE_FORMAT;
import static com.myicpc.tags.utils.TagConstants.VIDEO_FORMAT;

/**
 * @author Roman Smetana
 */
public abstract class NotificationTile {
    protected final Notification notification;
    protected final boolean isTemplate;
    protected final boolean editable;
    protected final Locale locale;
    protected final PageContext pageContext;

    public NotificationTile(Notification notification, boolean isTemplate, boolean editable, Locale locale, PageContext pageContext) {
        this.notification = notification;
        this.isTemplate = isTemplate;
        this.editable = editable;
        this.locale = locale != null ? locale : Locale.US;
        this.pageContext = pageContext;
    }

    public void render(JspWriter out) throws IOException, JspException {
        out.print("<div class=\"timelineTile clearfix\">");
        if (editable) {
            renderControl(out);
        }
        renderAvatar(out);
        out.print("<div class=\"media-body\">");
        out.print("<h4 class=\"media-heading\">");
        renderTitle(out);
        out.println("</h4>");
        renderBody(out);
        renderFooter(out);
        out.print("</div>");
        out.print("</div>");
    }

    protected abstract void renderAvatar(JspWriter out) throws IOException, JspException;

    protected void renderTitle(JspWriter out) throws IOException {
        out.print(getTitle());
    }

    protected void renderBody(JspWriter out) throws IOException, JspException {
        out.print("<p>");
        out.print(getBody());
        out.print("</p>");
    }

    protected void renderFooter(JspWriter out) throws IOException, JspException {
        out.print("<div class=\"footer\">");
        renderFooterTitle(out);
        renderFooterTimestamp(out);
        renderFooterShare(out);
        renderFooterAppendix(out);
        out.print("</div>");
    }

    protected abstract void renderFooterTitle(JspWriter out) throws IOException, JspException;

    protected void renderFooterTimestamp(JspWriter out) throws IOException {
        out.print(" <span class=\"hidden-xs\"> &middot; " + getTimestamp() + "</span> ");
    }

    protected void renderFooterAppendix(JspWriter out) throws IOException {
        // do nothing by default
    }

    protected void renderFooterShare(JspWriter out) throws IOException {
        String notificationId = isTemplate ? "{{id}}" : notification.getId().toString();
        out.print("&middot; ");
        out.print(String.format("<a href=\"javascript:showShareDialog(%s)\">", notificationId));
        out.print("<span class=\"glyphicon glyphicon-share-alt\"></span>");
        out.print(MessageUtils.getMessage("share", locale));
        out.print("</a>");
    }

    protected void renderAvatarImage(JspWriter out, String src, String title) throws IOException, JspException {
        out.print(String.format("<img class=\"pull-left media-object\" src=\"%s\" alt=\"%s\" width=\"%d\" height=\"%d\">", resolveUrl(src), title, 50, 50));
    }

    protected void renderAvatarIcon(JspWriter out, String iconClass) throws IOException {
        out.print(String.format("<span class=\"pull-left media-object %s\" style=\"font-size: 2.5em; width: 50px;\"></span>", iconClass));
    }

    protected String getTitle() {
        return isTemplate ? "{{title}}" : notification.getTitle();
    }

    protected String getBody() {
        if (isTemplate) {
            return "{{{body}}}";
        } else {
            return StringUtils.isNotEmpty(notification.getBody()) ? notification.getBody() : "";
        }
    }

    protected String getTimestamp() {
        if (isTemplate) {
            return "{{timestamp}}";
        }
        if (notification.getLocalTimestamp() == null) {
            return StringUtils.EMPTY;
        }
        DateFormat format = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, locale);
        return format.format(notification.getLocalTimestamp());
    }

    protected String getNotificationId() {
        if (isTemplate) {
            return "{{id}}";
        } else {
            return String.valueOf(notification.getId());
        }
    }

    protected String resolveUrl(String url) throws JspException {
        return JSPUtils.resolveUrl(url, pageContext);
    }

    protected void renderMedia(JspWriter out) throws IOException {
        if (isTemplate) {
            out.print(HandlebarsUtils.displayIfNotEmtpy("imageUrl", String.format(IMAGE_FORMAT, "{{imageUrl}}")));
            out.print(HandlebarsUtils.displayIfNotEmtpy("videoUrl", String.format(VIDEO_FORMAT, "{{videoUrl}}", "{{thumbnailUrl}}")));
        } else {
            if (!StringUtils.isEmpty(notification.getVideoUrl())) {
                out.print(String.format(VIDEO_FORMAT, notification.getVideoUrl(), notification.getThumbnailUrl()));
            } else if (!StringUtils.isEmpty(notification.getImageUrl())) {
                out.print(String.format(IMAGE_FORMAT, notification.getImageUrl()));
            }
        }
    }

    protected void renderControl(JspWriter out) throws IOException {
        out.write("<div class=\"text-right\">");
        out.write(String.format("<a href=\"javascript:void(0)\" onclick=\"deleteNotification(this, %s)\"><span class=\"glyphicon glyphicon-remove\"></span> %s</a>",
                getNotificationId(),
                MessageUtils.getMessage("delete")));
        String additionalControlButtons = additionalControlButtons();
        if (StringUtils.isNotEmpty(additionalControlButtons)) {
            out.write(" · ");
            out.write(additionalControlButtons);
        }
        out.write("</div>");
    }

    protected String additionalControlButtons() throws IOException {
        return null;
    }
}
