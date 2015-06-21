package com.myicpc.tags.kiosk;

import javax.servlet.jsp.PageContext;
import java.util.Locale;

/**
 * @author Roman Smetana
 */
public class QuestChallengeKioskTile extends KioskTile {
    public QuestChallengeKioskTile(Locale locale, PageContext pageContext) {
        super(locale, pageContext);
    }

    @Override
    protected String getTitleClass() {
        return "myicpc";
    }

    @Override
    protected String getTitleIcon() {
        return "glyphicon glyphicon-screenshot";
    }

    @Override
    protected String getBody() {
        return super.getBody() + getMedia();
    }
}
