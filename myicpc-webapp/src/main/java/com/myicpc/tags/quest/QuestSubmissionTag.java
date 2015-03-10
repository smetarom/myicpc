package com.myicpc.tags.quest;

import com.myicpc.model.quest.QuestSubmission;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

import static com.myicpc.tags.utils.TagConstants.IMAGE_FORMAT;
import static com.myicpc.tags.utils.TagConstants.VIDEO_FORMAT;

/**
 * @author Roman Smetana
 */
public class QuestSubmissionTag extends SimpleTagSupport {
    private QuestSubmission questSubmission;
    private boolean showDescription = true;

    public void setSubmission(QuestSubmission questSubmission) {
        this.questSubmission = questSubmission;
    }

    public void setShowDescription(boolean showDescription) {
        this.showDescription = showDescription;
    }

    @Override
    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();

        if (questSubmission != null) {
            if (showDescription && !StringUtils.isEmpty(questSubmission.getText())) {
                out.write(String.format("<p>%s</p>",questSubmission.getText()));
            }
            if (!StringUtils.isEmpty(questSubmission.getVideoUrl())) {
                out.print(String.format(VIDEO_FORMAT, questSubmission.getVideoUrl()));
            } else if (!StringUtils.isEmpty(questSubmission.getImageUrl())) {
                out.print(String.format(IMAGE_FORMAT, questSubmission.getImageUrl()));
            }
        }
    }
}
