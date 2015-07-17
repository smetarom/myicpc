package com.myicpc.tags.quest;

import com.myicpc.model.quest.QuestSubmission;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

import static com.myicpc.tags.utils.TagConstants.IMAGE_FORMAT;
import static com.myicpc.tags.utils.TagConstants.VIDEO_FORMAT;
import static com.myicpc.tags.utils.TagConstants.VIDEO_FORMAT_AUTOPLAY;

/**
 * @author Roman Smetana
 */
public class QuestSubmissionTag extends SimpleTagSupport {
    private QuestSubmission questSubmission;
    private boolean showDescription = true;
    private boolean videoAutoplay;

    public void setSubmission(QuestSubmission questSubmission) {
        this.questSubmission = questSubmission;
    }

    public void setShowDescription(boolean showDescription) {
        this.showDescription = showDescription;
    }

    public void setVideoAutoplay(boolean videoAutoplay) {
        this.videoAutoplay = videoAutoplay;
    }

    @Override
    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();

        if (questSubmission != null) {
            if (showDescription && !StringUtils.isEmpty(questSubmission.getNotification().getBody())) {
                out.write(String.format("<p>%s</p>",questSubmission.getNotification().getBody()));
            }
            if (!StringUtils.isEmpty(questSubmission.getNotification().getVideoUrl())) {
                String format = videoAutoplay ? VIDEO_FORMAT_AUTOPLAY : VIDEO_FORMAT;
                out.print(String.format(format, questSubmission.getNotification().getVideoUrl(), questSubmission.getNotification().getThumbnailUrl()));
            } else if (!StringUtils.isEmpty(questSubmission.getNotification().getImageUrl())) {
                out.print(String.format(IMAGE_FORMAT, questSubmission.getNotification().getImageUrl()));
            }
        }
    }
}
