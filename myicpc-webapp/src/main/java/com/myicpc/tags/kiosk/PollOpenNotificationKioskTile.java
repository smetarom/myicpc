package com.myicpc.tags.kiosk;

import com.myicpc.commons.utils.MessageUtils;

import javax.servlet.jsp.PageContext;
import java.util.Locale;

/**
 * @author Roman Smetana
 */
public class PollOpenNotificationKioskTile extends KioskTile {
    public PollOpenNotificationKioskTile(Locale locale, PageContext pageContext) {
        super(locale, pageContext);
    }

    @Override
    protected String getTitle() {
        return MessageUtils.getMessage("poll.opened") + ": " + super.getTitle();
    }

    @Override
    protected String getTitleClass() {
        return "myicpc";
    }

    @Override
    protected String getTitleIcon() {
        return "glyphicon glyphicon-bullhorn";
    }
}
