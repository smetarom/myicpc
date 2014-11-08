package com.myicpc.service.dto;

import com.google.common.base.Charsets;
import com.myicpc.model.Globals;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Roman Smetana
 */
public class GlobalSettings implements Serializable {
    private Map<Globals.GlobalsColumn, String> settingsMap = new HashMap<>();

    public GlobalSettings() {
    }

    public void addSettings(Globals.GlobalsColumn name, String value) {
        settingsMap.put(name, value);
    }

    public Map<Globals.GlobalsColumn, String> getSettingsMap() {
        return settingsMap;
    }

    public String getAdminEmail() {
        return settingsMap.get(Globals.GlobalsColumn.ADMIN_EMAIL);
    }

    public void setAdminEmail(String adminEmail) {
        settingsMap.put(Globals.GlobalsColumn.ADMIN_EMAIL, adminEmail);
    }

    public String getFbAPIKey() {
        return settingsMap.get(Globals.GlobalsColumn.FB_API_KEY);
    }

    public void setFbAPIKey(String fbAPIKey) {
        settingsMap.put(Globals.GlobalsColumn.FB_API_KEY, fbAPIKey);
    }

    public String getGoogleNonAuthenticatedKey() {
        return settingsMap.get(Globals.GlobalsColumn.GOOGLE_NON_AUTHENTICATED_KEY);
    }

    public void setGoogleNonAuthenticatedKey(String googleNonAuthenticatedKey) {
        settingsMap.put(Globals.GlobalsColumn.GOOGLE_NON_AUTHENTICATED_KEY, googleNonAuthenticatedKey);
    }

    public String getGoogleAnalyticsKey() {
        return settingsMap.get(Globals.GlobalsColumn.GOOGLE_ANALYTICS_KEY);
    }

    public void setGoogleAnalyticsKey(String googleAnalyticsKey) {
        settingsMap.put(Globals.GlobalsColumn.GOOGLE_ANALYTICS_KEY, googleAnalyticsKey);
    }

    public String getDefaultMapConfig() {
        return settingsMap.get(Globals.GlobalsColumn.DEFAULT_MAP_CONFIG);
    }

    public void setDefaultMapConfig(String defaultMapConfig) {
        settingsMap.put(Globals.GlobalsColumn.DEFAULT_MAP_CONFIG, defaultMapConfig);
    }

    public String getUniversityLogosURL() {
        return settingsMap.get(Globals.GlobalsColumn.UNIVERSITY_LOGOS_URL);
    }

    public void setUniversityLogosURL(String universityLogosURL) {
        settingsMap.put(Globals.GlobalsColumn.UNIVERSITY_LOGOS_URL, universityLogosURL);
    }

    public String getTeamPicturesURL() {
        return settingsMap.get(Globals.GlobalsColumn.TEAM_PICTURES_URL);
    }

    public void setTeamPicturesURL(String teamPicturesURL) {
        settingsMap.put(Globals.GlobalsColumn.TEAM_PICTURES_URL, teamPicturesURL);
    }
}
