package com.myicpc.model.contest;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class WebServiceSettings implements Serializable {
    private static final long serialVersionUID = 8156742203651195626L;

    //CM4
    private String wsCMToken;

    //Twitter
    private String twitterConsumerKey;
    private String twitterConsumerSecret;
    private String twitterAccessToken;
    private String twitterAccessTokenSecret;

    //YouTube
    private String youTubeUsername;
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd H:mm:ss")
    private Date youtubePullSince;

    //Instagram
    private String instagramKey;
    private String instagramSecret;

    //Vine
    private String vineEmail;
    private String vinePassword;

    private boolean showPicasaPhotos;

    public String getWsCMToken() {
        return wsCMToken;
    }

    public void setWsCMToken(String wsCMToken) {
        this.wsCMToken = wsCMToken;
    }

    public String getTwitterConsumerKey() {
        return twitterConsumerKey;
    }

    public void setTwitterConsumerKey(String twitterConsumerKey) {
        this.twitterConsumerKey = twitterConsumerKey;
    }

    public String getTwitterConsumerSecret() {
        return twitterConsumerSecret;
    }

    public void setTwitterConsumerSecret(String twitterConsumerSecret) {
        this.twitterConsumerSecret = twitterConsumerSecret;
    }

    public String getTwitterAccessToken() {
        return twitterAccessToken;
    }

    public void setTwitterAccessToken(String twitterAccessToken) {
        this.twitterAccessToken = twitterAccessToken;
    }

    public String getTwitterAccessTokenSecret() {
        return twitterAccessTokenSecret;
    }

    public void setTwitterAccessTokenSecret(String twitterAccessTokenSecret) {
        this.twitterAccessTokenSecret = twitterAccessTokenSecret;
    }

    public String getYouTubeUsername() {
        return youTubeUsername;
    }

    public void setYouTubeUsername(String youTubeUsername) {
        this.youTubeUsername = youTubeUsername;
    }

    public Date getYoutubePullSince() {
        return youtubePullSince;
    }

    public void setYoutubePullSince(Date youtubePullSince) {
        this.youtubePullSince = youtubePullSince;
    }

    public String getInstagramKey() {
        return instagramKey;
    }

    public void setInstagramKey(String instagramKey) {
        this.instagramKey = instagramKey;
    }

    public String getInstagramSecret() {
        return instagramSecret;
    }

    public void setInstagramSecret(String instagramSecret) {
        this.instagramSecret = instagramSecret;
    }

    public String getVineEmail() {
        return vineEmail;
    }

    public void setVineEmail(String vineEmail) {
        this.vineEmail = vineEmail;
    }

    public String getVinePassword() {
        return vinePassword;
    }

    public void setVinePassword(String vinePassword) {
        this.vinePassword = vinePassword;
    }

    public boolean isShowPicasaPhotos() {
        return showPicasaPhotos;
    }

    public void setShowPicasaPhotos(boolean showPicasaPhotos) {
        this.showPicasaPhotos = showPicasaPhotos;
    }

}
