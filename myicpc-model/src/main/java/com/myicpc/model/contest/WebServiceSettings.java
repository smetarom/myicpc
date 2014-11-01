package com.myicpc.model.contest;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class WebServiceSettings implements Serializable {
    private static final long serialVersionUID = 8156742203651195626L;

    private String wsCMToken;

    private String twitterConsumerKey;
    private String twitterConsumerSecret;
    private String twitterAccessToken;
    private String twitterAccessTokenSecret;

    private String youTubeUsername;

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

    public boolean isShowPicasaPhotos() {
        return showPicasaPhotos;
    }

    public void setShowPicasaPhotos(boolean showPicasaPhotos) {
        this.showPicasaPhotos = showPicasaPhotos;
    }

}
