package com.myicpc.tags.notification;

import com.myicpc.commons.utils.MessageUtils;
import com.myicpc.model.social.Notification;
import com.myicpc.tags.notification.SocialTile;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import java.io.IOException;
import java.util.Locale;

/**
 * @author Roman Smetana
 */
public class TwitterTile extends SocialTile {

    public TwitterTile(Notification notification, boolean isTemplate, Locale locale, PageContext pageContext) {
        super(notification, isTemplate, locale, pageContext);
    }

    @Override
    protected String getTitleFormat() {
        return "%s <small>@%s</small>";
    }

    @Override
    protected void renderFooterAppendix(JspWriter out) throws IOException {
        Object[] params = new Object[2];
        if (isTemplate) {
            params[0] = "{{externalId}}";
        } else {
            params[0] = notification.getExternalId();
        }
        params[1] = MessageUtils.getMessage("retweet", locale);
        out.write(String.format(" &middot; <a href=\"https://twitter.com/intent/retweet?tweet_id=%s\"><span class=\"glyphicon glyphicon-retweet\"></span> %s</a>", params));
    }

    @Override
    protected void renderFooterTitle(JspWriter out) throws IOException, JspException {
        out.print(String.format("<span class=\"fa fa-twitter\"></span> %s", MessageUtils.getMessage("twitter", locale)));
    }
}
