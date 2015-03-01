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
public class PicasaTile extends SocialTile {

    public PicasaTile(Notification notification, boolean isTemplate, Locale locale, PageContext pageContext) {
        super(notification, isTemplate, locale, pageContext);
    }

    @Override
    protected String getTitleFormat() {
        return "%s";
    }

    @Override
    protected void renderAvatar(JspWriter out) throws IOException, JspException {
        renderAvatarIcon(out, "glyphicon glyphicon-camera");
    }

    @Override
    protected void renderFooterTitle(JspWriter out) throws IOException, JspException {
        out.print(String.format("<span class=\"glyphicon glyphicon-camera\"></span> %s", MessageUtils.getMessage("crowdGallery.picasa", locale)));
    }
}
