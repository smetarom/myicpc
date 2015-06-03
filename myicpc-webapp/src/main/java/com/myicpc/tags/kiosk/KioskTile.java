package com.myicpc.tags.kiosk;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import java.io.IOException;
import java.util.Locale;

/**
 * @author Roman Smetana
 */
public abstract class KioskTile {
    protected final Locale locale;
    protected final PageContext pageContext;

    public KioskTile(Locale locale, PageContext pageContext) {
        this.locale = locale != null ? locale : Locale.US;
        this.pageContext = pageContext;
    }

    public void render(JspWriter out) throws IOException, JspException {
        out.print(String.format("<div class=\"tile-title %s\">", getTitleClass()));
        if (StringUtils.isNotEmpty(getTitleIcon())) {
            out.print(String.format("<div class=\"pull-right %s\"></div>", getTitleIcon()));
        }
        if (StringUtils.isNotEmpty(getTitleImageUrl())) {
            out.print(String.format("<img ng-src=\"%s\" alt=\"%s\" width=\"48\" height=\"48\" /> ", getTitleImageUrl(), getTitleImageAlt()));
        }
        out.print(getTitle());
        out.print("</div>");
        out.print("<div class=\"tile-detail\">");
        out.print(getBody());
        out.print("</div>");
    }

    protected String getTitle() {
        return "{{notification.title}}";
    }

    protected String getTitleClass() {
        return "";
    }

    protected String getTitleIcon() {
        return null;
    }

    protected String getTitleImageUrl() {
        return null;
    }

    protected String getTitleImageAlt() {
        return "";
    }

    protected String getBody() {
        return "<p ng-bind-html=\"trustedHTML(notification.body)\"></p>";
    }

    protected String getMedia() {
        return getVideo() +
                "<div ng-if=\"!notification.videoUrl\">" +
                getImage() +
                "</div>";
    }

    protected String getImage() {
        return "<div ng-if=\"notification.imageUrl\" style=\"min-height: 350px;\">" +
                "   <div>" +
                "       <img alt=\"{{notification.title}}\" ng-src=\"{{notification.imageUrl}}\" class=\"center-block\" style=\"max-height: 300px; max-width: 100%\">" +
                "   </div>" +
                "</div>";
    }

    protected String getVideo() {
        return "<div ng-if=\"notification.videoUrl\" style=\"min-height: 350px;\">" +
                "   <video width=\"300\" height=\"300\" class=\"center-block\" autoplay muted loop>\n" +
                "       <source ng-src=\"{{trustedResource(notification.videoUrl)}}\" type=\"video/mp4\">\n" +
                "   </video>" +
                "</div>";
    }
}
