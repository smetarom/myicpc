package com.myicpc.dto.eventFeed.parser;

import java.io.Serializable;

/**
 * Holder for event feed additional settings
 *
 * @author Roman Smetana
 */
public class EventFeedSettingsDTO implements Serializable {
    private static final long serialVersionUID = -6500214109208697495L;

    private boolean skipMessageGeneration;

    public boolean isSkipMessageGeneration() {
        return skipMessageGeneration;
    }

    public void setSkipMessageGeneration(boolean skipMessageGeneration) {
        this.skipMessageGeneration = skipMessageGeneration;
    }
}
