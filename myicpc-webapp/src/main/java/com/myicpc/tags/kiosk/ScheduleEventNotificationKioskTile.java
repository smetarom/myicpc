package com.myicpc.tags.kiosk;

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
}
