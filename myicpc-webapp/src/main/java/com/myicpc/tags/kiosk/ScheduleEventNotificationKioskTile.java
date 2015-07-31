package com.myicpc.tags.kiosk;

import com.myicpc.commons.utils.MessageUtils;

import javax.servlet.jsp.PageContext;
import java.util.Locale;

/**
 * @author Roman Smetana
 */
public class ScheduleEventNotificationKioskTile extends KioskTile {
    public ScheduleEventNotificationKioskTile(Locale locale, PageContext pageContext) {
        super(locale, pageContext);
    }

    @Override
    protected String getTitleClass() {
        return "myicpc";
    }

    @Override
    protected String getTitleIcon() {
        return "glyphicon glyphicon-calendar";
    }

    @Override
    protected String getBody() {
        String timeInfo = "<strong>%s:</strong> %s - %s";
        String locationInfo = "<strong>%s:</strong> %s";
        String rolesInfo = "<strong>%s:</strong> %s";
        String thingsToBringInfo = "<strong>%s:</strong> %s";

        StringBuilder body = new StringBuilder();
        body.append(String.format(timeInfo, MessageUtils.getMessage("schedule.time", locale), "{{notification.body.startDate}}", "{{notification.body.endDate}}"));

        body.append("<div ng-if=\"notification.body.location\">");
        body.append(String.format(locationInfo, MessageUtils.getMessage("event.location", locale), "{{notification.body.location}}"));
        body.append("</div>");

        body.append("<div ng-if=\"notification.body.roles\">");
        body.append(String.format(rolesInfo, MessageUtils.getMessage("schedule.attendees", locale), "{{notification.body.roles}}"));
        body.append("</div>");

        body.append("<div ng-if=\"notification.body.thingsToBring\">");
        body.append(String.format(thingsToBringInfo, MessageUtils.getMessage("schedule.thingsToBring", locale), "{{notification.body.thingsToBring}}"));
        body.append("</div>");

        return body.toString();
    }
}
