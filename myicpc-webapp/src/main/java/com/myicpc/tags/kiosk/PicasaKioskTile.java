package com.myicpc.tags.kiosk;

import com.myicpc.commons.utils.MessageUtils;

import javax.servlet.jsp.PageContext;
import java.util.Locale;

/**
 * @author Roman Smetana
 */
public class PicasaKioskTile extends KioskTile {
    public PicasaKioskTile(Locale locale, PageContext pageContext) {
        super(locale, pageContext);
    }

    @Override
    protected String getTitle() {
        return MessageUtils.getMessage("crowdGallery");
    }

    @Override
    protected String getTitleClass() {
        return "myicpc";
    }

    @Override
    protected String getTitleIcon() {
        return "glyphicon glyphicon-camera";
    }

    @Override
    protected String getBody() {
        return super.getBody() + getMedia();
    }
}
