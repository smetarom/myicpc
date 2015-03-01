package com.myicpc.social.dto;

import java.io.Serializable;

/**
 * Represents a wrapper for Picasa photo
 *
 * @author Roman Smetana
 */
public class PicasaPhoto implements Serializable {
    /**
     * Picasa photo ID
     */
    private long picasaId;
    /**
     * Photo title
     */
    private String title;
    /**
     * Photo URL
     */
    private String url;
    /**
     * URL of photo thumbnail
     */
    private String thumbnailUrl;
    /**
     * Number of photo comments
     */
    private int commentCount;

    public PicasaPhoto() {
    }

    public PicasaPhoto(final long picasaId, final String title, final String url) {
        this.picasaId = picasaId;
        this.title = title;
        this.url = url;
    }

    public long getPicasaId() {
        return picasaId;
    }

    public void setPicasaId(final long picasaId) {
        this.picasaId = picasaId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(final String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(final int commentCount) {
        this.commentCount = commentCount;
    }
}
