package com.myicpc.model.rss;

import com.myicpc.model.IdGeneratedContestObject;
import com.myicpc.model.contest.Contest;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Represents RSS feed
 *
 * @author Roman Smetana
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"url", "contestId"}))
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "RSSFeed_id_seq")
public class RSSFeed extends IdGeneratedContestObject {
    private static final long serialVersionUID = -5305396635139792677L;

    /**
     * Name of the feed
     */
    private String name;
    /**
     * Feed URL
     */
    private String url;
    /**
     * Base URL to the source
     */
    private String linkToSource;
    /**
     * Last timestamp, when the messages were pulled from the feed
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastPulledDate;
    /**
     * Is feed activated?
     */
    private boolean disabled;
    /**
     * Number of messages from the feed
     */
    @Transient
    private int messageCount;

    /**
     * List of RSS messages from the feed
     */
    @OneToMany(mappedBy = "rssFeed", cascade = CascadeType.ALL)
    private List<RSSMessage> rssMessages;

    public RSSFeed() {
    }

    public RSSFeed(Contest contest) {
        super(contest);
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getLinkToSource() {
        return linkToSource;
    }

    public void setLinkToSource(final String linkToSource) {
        this.linkToSource = linkToSource;
    }

    public Date getLastPulledDate() {
        return lastPulledDate;
    }

    public void setLastPulledDate(final Date lastPulledDate) {
        this.lastPulledDate = lastPulledDate;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(final boolean disabled) {
        this.disabled = disabled;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(final int messageCount) {
        this.messageCount = messageCount;
    }

    public List<RSSMessage> getRssMessages() {
        return rssMessages;
    }

    public void setRssMessages(final List<RSSMessage> rssMessages) {
        this.rssMessages = rssMessages;
    }
}
