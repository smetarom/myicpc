package com.myicpc.tags.kiosk;

import javax.servlet.jsp.PageContext;
import java.util.Locale;

/**
 * @author Roman Smetana
 */
public class AdminNotificationKioskTile extends KioskTile {
    public AdminNotificationKioskTile(Locale locale, PageContext pageContext) {
        super(locale, pageContext);
    }

    @Override
    protected String getTitleClass() {
        return "myicpc";
    }

    @Override
    protected String getTitleIcon() {
        return "fa fa-exclamation-triangle";
    }
}
