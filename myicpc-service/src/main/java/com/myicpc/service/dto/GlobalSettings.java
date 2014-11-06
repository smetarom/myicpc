package com.myicpc.service.dto;

import com.google.common.base.Charsets;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.nio.charset.Charset;

/**
 * @author Roman Smetana
 */
public class GlobalSettings implements Serializable {
    private String adminEmail;

    private String fbAPIKey;
    private String googleNonAuthenticatedKey;
    private String googleAnalyticsKey;
    private String defaultMapConfig;

    public GlobalSettings() {
    }

    public GlobalSettings(String defaultMapConfig) {
        this.defaultMapConfig = defaultMapConfig;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public String getFbAPIKey() {
        return fbAPIKey;
    }

    public void setFbAPIKey(String fbAPIKey) {
        this.fbAPIKey = fbAPIKey;
    }

    public String getGoogleNonAuthenticatedKey() {
        return googleNonAuthenticatedKey;
    }

    public void setGoogleNonAuthenticatedKey(String googleNonAuthenticatedKey) {
        this.googleNonAuthenticatedKey = googleNonAuthenticatedKey;
    }

    public String getGoogleAnalyticsKey() {
        return googleAnalyticsKey;
    }

    public void setGoogleAnalyticsKey(String googleAnalyticsKey) {
        this.googleAnalyticsKey = googleAnalyticsKey;
    }

    public String getDefaultMapConfig() {
        return defaultMapConfig;
    }

    public void setDefaultMapConfig(String defaultMapConfig) {
        this.defaultMapConfig = defaultMapConfig;
    }
}
