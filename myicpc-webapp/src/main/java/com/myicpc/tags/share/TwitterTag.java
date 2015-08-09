package com.myicpc.tags.share;

import com.myicpc.commons.utils.MessageUtils;
import com.myicpc.model.social.Notification;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.Locale;

/**
 * @author Roman Smetana
 */
public class TwitterTag extends SimpleTagSupport {
    private Notification notification;

    private Locale locale;

    public TwitterTag() {
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

        String shareUrl = "https://twitter.com/intent/tweet?" +
                "hashtags=" + notification.getContest().getHashtag();

        if (StringUtils.isNotEmpty(imageUrl)) {
            shareUrl += "&url=" + imageUrl;
        }

        out.print(String.format("<a href=\"javascript:void(0)\" onclick=\"window.open('%s', 'facebook-share-dialog', 'width=626,height=436');\">", shareUrl));
        out.print("<span class=\"fa fa-twitter-square\"></span> ");
        out.print(MessageUtils.getMessage("twitter", locale));
        out.print("</a>");
    }

    private static String getImage(Notification notification) {
        if (StringUtils.isNotEmpty(notification.getImageUrl())) {
            return notification.getImageUrl();
        }
        return notification.getThumbnailUrl();
    }

}
