package com.myicpc.social.dto;

import com.myicpc.model.contest.WebServiceSettings;
import twitter4j.TwitterStream;

import java.io.Serializable;
import java.util.Objects;

/**
 * Data holder for {@link TwitterStream} and its configuration
 *
 * @author Roman Smetana
 */
public class TwitterStreamDTO implements Serializable {
    private static final long serialVersionUID = -664673224335227719L;

    private String hashtag;
    private String twitterConsumerKey;
    private String twitterConsumerSecret;
    private String twitterAccessToken;
    private String twitterAccessTokenSecret;

    private TwitterStream twitterStream;

    public TwitterStreamDTO(TwitterStream twitterStream, String hashtag, String twitterConsumerKey, String twitterConsumerSecret, String twitterAccessToken, String twitterAccessTokenSecret) {
        this.twitterStream = twitterStream;
        this.hashtag = hashtag;
        this.twitterConsumerKey = twitterConsumerKey;
        this.twitterConsumerSecret = twitterConsumerSecret;
        this.twitterAccessToken = twitterAccessToken;
        this.twitterAccessTokenSecret = twitterAccessTokenSecret;
    }

    public TwitterStream getTwitterStream() {
        return twitterStream;
    }

    /**
     * Detects if the {@code hashtag} or {@code webServiceSettings} have changed
     *
     * @param hashtag followed hashtag
     * @param webServiceSettings twitter configuration
     * @return any twitter parameter has changed
     */
    public boolean hasConfigChanged(String hashtag, WebServiceSettings webServiceSettings) {
        if (webServiceSettings == null) {
            return false;
        }

        return !Objects.equals(this.hashtag, hashtag) ||
                !Objects.equals(twitterConsumerKey, webServiceSettings.getTwitterConsumerKey()) ||
                !Objects.equals(twitterConsumerSecret, webServiceSettings.getTwitterConsumerSecret()) ||
                !Objects.equals(twitterAccessToken, webServiceSettings.getTwitterAccessToken()) ||
                !Objects.equals(twitterAccessTokenSecret, webServiceSettings.getTwitterAccessTokenSecret());
    }
}
