package com.myicpc.tags.notification;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.myicpc.commons.adapters.JSONAdapter;
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
public class EventOpenTile extends NotificationTile {

    public EventOpenTile(Notification notification, boolean isTemplate, boolean editable, Locale locale, PageContext pageContext) {
        super(notification, isTemplate, editable, locale, pageContext);
    }

    @Override
    protected void renderAvatar(JspWriter out) throws IOException, JspException {
        renderAvatarIcon(out, "glyphicon glyphicon-bell");
    }

    @Override
    protected void renderBody(JspWriter out) throws IOException, JspException {
        String timeInfo = "<strong>%s:</strong> %s - %s";
        String locationInfo = "</br><strong>%s:</strong> %s";
        String rolesInfo = "</br><strong>%s:</strong> %s";
        String thingsToBringInfo = "</br><strong>%s:</strong> %s";
        if (isTemplate) {
            out.print(String.format(timeInfo, MessageUtils.getMessage("schedule.time", locale), "{{body.startDate}}", "{{body.endDate}}"));

            out.print("{{#if body.location}}");
            out.print(String.format(locationInfo, MessageUtils.getMessage("event.location", locale), "{{body.location}}"));
            out.print("{{/if}}");

            out.print("{{#if body.roles}}");
            out.print(String.format(rolesInfo, MessageUtils.getMessage("schedule.attendees", locale), "{{body.roles}}"));
            out.print("{{/if}}");

            out.print("{{#if body.thingsToBring}}");
            out.print(String.format(thingsToBringInfo, MessageUtils.getMessage("schedule.thingsToBring", locale), "{{body.thingsToBring}}"));
            out.print("{{/if}}");
        } else {
            JsonObject bodyObject = new JsonParser().parse(notification.getBody()).getAsJsonObject();
            JSONAdapter bodyAdapter = new JSONAdapter(bodyObject);

            out.print(String.format(timeInfo, MessageUtils.getMessage("schedule.time", locale), bodyAdapter.getString("startDate"), bodyAdapter.getString("endDate")));

            String location = bodyAdapter.getString("location");
            if (StringUtils.isNotEmpty(location)) {
                out.print(String.format(locationInfo, MessageUtils.getMessage("event.location", locale), location));
            }

            String roles = bodyAdapter.getString("roles");
            if (StringUtils.isNotEmpty(roles)) {
                out.print(String.format(rolesInfo, MessageUtils.getMessage("schedule.attendees", locale), roles));
            }

            String thingsToBring = bodyAdapter.getString("thingsToBring");
            if (StringUtils.isNotEmpty(thingsToBring)) {
                out.print(String.format(thingsToBringInfo, MessageUtils.getMessage("schedule.thingsToBring", locale), thingsToBring));
            }
        }
    }

    @Override
    protected void renderFooterTitle(JspWriter out) throws IOException, JspException {
        out.print(String.format("<span class=\"glyphicon glyphicon-bell\"></span> %s", MessageUtils.getMessage("timeline.tile.schedule", locale)));
    }
}
