package com.myicpc.dto.eventFeed;

import java.io.Serializable;

/**
 * @author Roman Smetana
 */
public class EventFeedResponse implements Serializable {
    private int code;

    public EventFeedResponse(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
