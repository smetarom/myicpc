package com.myicpc.tags.kiosk;

import javax.servlet.jsp.PageContext;
import java.util.Locale;

/**
 * @author Roman Smetana
 */
public class YoutubeKioskTile extends KioskTile {
    public YoutubeKioskTile(Locale locale, PageContext pageContext) {
        super(locale, pageContext);
    }

    @Override
    protected String getTitle() {
        return "{{notification.body}}";
    }

    @Override
    protected String getTitleClass() {
        return "youtube";
    }

    @Override
    protected String getTitleIcon() {
        return "fa fa-youtube-square";
    }

    @Override
    protected String getBody() {
        return "<iframe width=\"300\" height=\"225\" frameBorder=\"0\" class=\"center-block\" ng-src=\"{{trustedYoutubeResource(notification.videoUrl)}}\"></iframe>";
    }
}
