package com.myicpc.model.social;

import com.myicpc.model.IdGeneratedContestObject;

import java.util.Date;

/**
 * General class for entities created from social networks
 *
 * @author Roman Smetana
 */
public abstract class ASocialMedia extends IdGeneratedContestObject {
    private static final long serialVersionUID = -7262150277411934346L;

    /**
     * @return author social username
     */
    public abstract String getUsername();

    /**
     * @return the content of the post
     */
    public abstract String getDescription();

    /**
     * @return URL of the media associated with the post
     */
    public abstract String getMediaURL();

    /**
     * @return timestamp, when the post was posted
     */
    public abstract Date getTimestamp();

}
