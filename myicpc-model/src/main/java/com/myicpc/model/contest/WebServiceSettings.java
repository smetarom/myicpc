package com.myicpc.model.contest;

import com.myicpc.model.IdGeneratedObject;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Cacheable;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Cacheable
@Entity
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "WebServiceSettings_id_seq")
public class WebServiceSettings extends IdGeneratedObject {
    private static final long serialVersionUID = 8156742203651195626L;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "mapConfiguration")
    private Contest contest;

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
    private String showPhotosUsername;
    private String picasaUsername;
    private String picasaPassword;
    private String picasaCrowdAlbumId;
    private String picasaPrivateAlbumId;

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

    public String getShowPhotosUsername() {
        return showPhotosUsername;
    }

    public void setShowPhotosUsername(String showPhotosUsername) {
        this.showPhotosUsername = showPhotosUsername;
    }

    public String getPicasaUsername() {
        return picasaUsername;
    }

    public void setPicasaUsername(String picasaUsername) {
        this.picasaUsername = picasaUsername;
    }

    public String getPicasaPassword() {
        return picasaPassword;
    }

    public void setPicasaPassword(String picasaPassword) {
        this.picasaPassword = picasaPassword;
    }

    public String getPicasaCrowdAlbumId() {
        return picasaCrowdAlbumId;
    }

    public void setPicasaCrowdAlbumId(String picasaCrowdAlbumId) {
        this.picasaCrowdAlbumId = picasaCrowdAlbumId;
    }

    public String getPicasaPrivateAlbumId() {
        return picasaPrivateAlbumId;
    }

    public void setPicasaPrivateAlbumId(String picasaPrivateAlbumId) {
        this.picasaPrivateAlbumId = picasaPrivateAlbumId;
    }
}
