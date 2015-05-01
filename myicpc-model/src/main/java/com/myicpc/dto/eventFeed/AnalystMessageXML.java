package com.myicpc.dto.eventFeed;

import com.myicpc.dto.eventFeed.visitor.EventFeedVisitor;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.social.Notification;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author Roman Smetana
 */
@XStreamAlias("analystmsg")
public class AnalystMessageXML extends XMLEntity<Notification> {
    @XStreamAlias("run_id")
    private Long runId;
    @XStreamAlias("team")
    private Long teamId;
    private String title;
    private String message;

    @Override
    public void mergeTo(Notification notification) {
        notification.setTitle(getTitle());
        notification.setBody(getMessage());
    }

    @Override
    public void accept(EventFeedVisitor visitor, Contest contest) {
        visitor.visit(this, contest);
    }

    public Long getRunId() {
        return runId;
    }

    public void setRunId(Long runId) {
        this.runId = runId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
