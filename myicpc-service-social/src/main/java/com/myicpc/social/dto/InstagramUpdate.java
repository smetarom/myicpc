package com.myicpc.social.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author Roman Smetana
 */
public class InstagramUpdate implements Serializable {

    @JsonProperty("subscription_id")
    private Integer subscriptionId;
    private String object;
    @JsonProperty("object_id")
    private String objectId;
    @JsonProperty("changed_aspect")
    private String changedAspect;

    public Integer getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Integer subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }


    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getChangedAspect() {
        return changedAspect;
    }

    public void setChangedAspect(String changedAspect) {
        this.changedAspect = changedAspect;
    }

    @Override
    public String toString() {
        return "InstagramUpdate{" +
                "subscriptionId=" + subscriptionId +
                ", object='" + object + '\'' +
                ", objectId='" + objectId + '\'' +
                ", changedAspect='" + changedAspect + '\'' +
                '}';
    }
}
