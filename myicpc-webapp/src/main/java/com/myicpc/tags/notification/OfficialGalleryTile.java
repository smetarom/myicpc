package com.myicpc.tags.notification;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
public class OfficialGalleryTile extends NotificationTile {

    public OfficialGalleryTile(Notification notification, boolean isTemplate, boolean editable, Locale locale, PageContext pageContext) {
        super(notification, isTemplate, editable, locale, pageContext);
    }

    @Override
    protected void renderAvatar(JspWriter out) throws IOException, JspException {
        renderAvatarIcon(out, "glyphicon glyphicon-camera");
    }

    @Override
    protected void renderFooterTitle(JspWriter out) throws IOException, JspException {
        out.print(String.format("<span class=\"glyphicon glyphicon-camera\"></span> %s", MessageUtils.getMessage("officialGallery", locale)));
    }

    @Override
    protected void renderBody(JspWriter out) throws IOException, JspException {
        String thumbnailHTML = "<td style=\"width: 50%%; padding: 1px;\"><a href=\"#\"><img src=\"%s\" alt=\"\" class=\"img-responsive\" style=\"margin: 0;\" /></a></td>";
        if (isTemplate) {

        } else {
            JsonObject bodyObject = new JsonParser().parse(notification.getBody()).getAsJsonObject();
            int count = bodyObject.get("count").getAsInt();
            JsonArray photos = bodyObject.getAsJsonArray("photos");
            out.print("<table>");
            out.print("<tr>");
            for (int i = 0; i < 4; i++) {
                if (i == 2) {
                    out.print("</tr><tr>");
                }
                if (i == 3 && photos.size() < count) {
                    out.print(String.format("<td class=\"last-official-photo text-center\">" +
                            "<a href=\"javascript:showOfficialGallery('%s')\" class=\"block\">+%s</a>" +
                            "</td>", bodyObject.get("tag").getAsString(), count + 1 - photos.size()));
                } else if (i < photos.size()) {
                    out.print(String.format(thumbnailHTML, photos.get(i).getAsString()));
                }
            }
            out.print("</tr>");
            out.print("</table>");
        }
    }
}
