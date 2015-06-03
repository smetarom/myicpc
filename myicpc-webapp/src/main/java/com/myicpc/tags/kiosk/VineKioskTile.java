package com.myicpc.tags.kiosk;

import javax.servlet.jsp.PageContext;
import java.util.Locale;

/**
 * @author Roman Smetana
 */
public class VineKioskTile extends KioskTile {
    public VineKioskTile(Locale locale, PageContext pageContext) {
        super(locale, pageContext);
    }

    @Override
    protected String getTitle() {
        return "{{notification.authorName}}";
    }

    @Override
    protected String getTitleClass() {
        return "vine";
    }

    @Override
    protected String getTitleIcon() {
        return "fa fa-vine";
    }

    @Override
    protected String getTitleImageUrl() {
        return "{{notification.profileUrl}}";
    }

    @Override
    protected String getTitleImageAlt() {
        return "{{notification.title}}";
    }

    @Override
    protected String getBody() {
        return super.getBody() + getMedia();
    }
}
