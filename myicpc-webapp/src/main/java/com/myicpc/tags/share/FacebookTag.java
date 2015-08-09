package com.myicpc.tags.share;

import com.myicpc.commons.utils.FormatUtils;
import com.myicpc.commons.utils.MessageUtils;
import com.myicpc.model.social.Notification;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Locale;

/**
 * @author Roman Smetana
 */
public class FacebookTag extends SimpleTagSupport {
    private Notification notification;

    private Locale locale;

    public FacebookTag() {
        if (getJspContext() != null && getJspContext().getELContext() != null) {
            locale = getJspContext().getELContext().getLocale();
        }
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    @Override
    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();

        String imageUrl = getImage(notification);

        String shareUrl = "https://www.facebook.com/dialog/feed?" +
                "app_id=145634995501895" +
                "&display=popup" +
                "&name=" + notification.getTitle() +
                "&caption=" + MessageUtils.getMessage("app.name", locale) +
                "&description=" + URLEncoder.encode(getDescription(notification), FormatUtils.DEFAULT_ENCODING.name()) +
                "&link=https%3A%2F%2Fdevelopers.facebook.com%2Fdocs%2F" +
                "&redirect_uri=https://developers.facebook.com/tools/explorer";

        if (StringUtils.isNotEmpty(imageUrl)) {
            shareUrl += "&picture=" + imageUrl;
        }

        out.print(String.format("<a href=\"javascript:void(0)\" onclick=\"window.open('%s', 'facebook-share-dialog', 'width=626,height=436');\">", shareUrl));
        out.print("<span class=\"fa fa-facebook-square\"></span> ");
        out.print(MessageUtils.getMessage("facebook", locale));
        out.print("</a>");
    }

    private static String getDescription(Notification notification) {
        return FormatUtils.removeHTMLTags(notification.getBody());
    }

    private static String getImage(Notification notification) {
        if (StringUtils.isNotEmpty(notification.getImageUrl())) {
            return notification.getImageUrl();
        }
        return notification.getThumbnailUrl();
    }

}
