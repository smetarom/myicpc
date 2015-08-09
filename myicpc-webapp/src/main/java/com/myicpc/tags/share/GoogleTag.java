package com.myicpc.tags.share;

import com.myicpc.commons.utils.FormatUtils;
import com.myicpc.commons.utils.MessageUtils;
import com.myicpc.model.social.Notification;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.Locale;

/**
 * @author Roman Smetana
 */
public class GoogleTag extends SimpleTagSupport {
    private Notification notification;

    private Locale locale;

    public GoogleTag() {
        if (getJspContext() != null && getJspContext().getELContext() != null) {
            locale = getJspContext().getELContext().getLocale();
        }
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    @Override
    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();

        String javascript = "<script>" +
                "  var options = {" +
                "    contenturl: window.location.href," +
                "    contentdeeplinkid: '/pages'," +
                "    clientid: '802619831122.apps.googleusercontent.com'," +
                "    cookiepolicy: 'single_host_origin'," +
                "    prefilltext: '" + getDescription(notification) + "'," +
                "    calltoactionlabel: 'VISIT'," +
                "    calltoactionurl: 'http://plus.google.com/pages/create'," +
                "    calltoactiondeeplinkid: '/pages/create'" +
                "  };" +
                "  gapi.interactivepost.render('shareGooglePost" + notification.getId() + "', options);\n" +
                "</script>";
        out.print("<a href=\"javascript:void(0)\" id=\"shareGooglePost" + notification.getId() + "\">");
        out.print("<span class=\"fa fa-google-plus-square\"></span> ");
        out.print(MessageUtils.getMessage("google", locale));
        out.print("</a>");
        out.print(javascript);

    }

    private static String getDescription(Notification notification) {
        return FormatUtils.removeHTMLTags(notification.getBody());
    }

}
