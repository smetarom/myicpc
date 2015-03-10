package com.myicpc.tags.notification;

import com.myicpc.model.social.Notification;
import com.myicpc.tags.utils.JSPUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Locale;

/**
 * @author Roman Smetana
 */
public abstract class NotificationTile {
    protected Notification notification;
    protected boolean isTemplate;
    protected Locale locale;
    protected PageContext pageContext;

    public NotificationTile(Notification notification, boolean isTemplate, Locale locale, PageContext pageContext) {
        this.notification = notification;
        this.isTemplate = isTemplate;
        this.locale = locale != null ? locale : Locale.US;
        this.pageContext = pageContext;
    }

    public void render(JspWriter out) throws IOException, JspException {
        out.print("<div class=\"timelineTile clearfix\">");
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
        renderFooterAppendix(out);
        out.print("</div>");
    }

    protected abstract void renderFooterTitle(JspWriter out) throws IOException, JspException;

    protected void renderFooterTimestamp(JspWriter out) throws IOException {
        out.print(" <span class=\"hidden-xs\"> &middot; " + getTimestamp() + "</span> ");
    }

    protected void renderFooterAppendix(JspWriter out) throws IOException, JspException {
        // do nothing by default
    }

    protected void renderAvatarImage(JspWriter out, String src, String title) throws IOException, JspException {
        out.print(String.format("<img class=\"pull-left media-object\" src=\"%s\" alt=\"%s\" width=\"%d\" height=\"%d\">", resolveUrl(src), title, 50, 50));
    }

    protected void renderAvatarIcon(JspWriter out, String iconClass) throws IOException, JspException {
        out.print(String.format("<span class=\"pull-left media-object %s\" style=\"font-size: 2.5em; width: 50px;\"></span>", iconClass));
    }

    protected String getTitle() {
        return isTemplate ? "{{title}}" : notification.getTitle();
    }

    protected String getBody() {
        return isTemplate ? "{{{body}}}" : notification.getBody();
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

    protected String resolveUrl(String url) throws JspException {
        return JSPUtils.resolveUrl(url, pageContext);
    }
}
