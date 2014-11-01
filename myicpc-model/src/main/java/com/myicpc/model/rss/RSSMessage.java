package com.myicpc.model.rss;

import com.myicpc.model.IdGeneratedObject;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Represents a message from RSS Feed
 *
 * @author Roman Smetana
 */
@Entity
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "RSSMessage_id_seq")
public class RSSMessage extends IdGeneratedObject {
    private static final long serialVersionUID = -5956695387432633218L;

    /**
     * Title of the message
     */
    private String name;
    /**
     * Email of the author of the message
     */
    private String authorEmail;
    /**
     * URL to message
     */
    private String url;
    /**
     * Content of the message
     */
    @Lob
    @Type(type = "org.hibernate.type.StringClobType")
    private String text;
    /**
     * Publish date of the message
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date publishDate;

    /**
     * From RSS feed
     */
    @NotNull
    @ManyToOne
    @JoinColumn(name = "rssFeedId")
    private RSSFeed rssFeed;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(final String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(final Date publishDate) {
        this.publishDate = publishDate;
    }

    public RSSFeed getRssFeed() {
        return rssFeed;
    }

    public void setRssFeed(final RSSFeed rssFeed) {
        this.rssFeed = rssFeed;
    }
}
