package com.myicpc.model.contest;

import com.myicpc.model.IdGeneratedObject;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;

/**
 * @author Roman Smetana
 */
@Cacheable
@Entity
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "ModuleConfiguration_id_seq")
public class ModuleConfiguration extends IdGeneratedObject {
    private boolean mapModule;
    private boolean codeInsightModule;
    private boolean scheduleModule;
    private boolean rssModule;
    private boolean pollModule;
    private boolean galleryModule;
    private boolean questModule;

    public boolean isMapModule() {
        return mapModule;
    }

    public void setMapModule(boolean mapModule) {
        this.mapModule = mapModule;
    }

    public boolean isCodeInsightModule() {
        return codeInsightModule;
    }

    public void setCodeInsightModule(boolean codeInsightModule) {
        this.codeInsightModule = codeInsightModule;
    }

    public boolean isScheduleModule() {
        return scheduleModule;
    }

    public void setScheduleModule(boolean scheduleModule) {
        this.scheduleModule = scheduleModule;
    }

    public boolean isRssModule() {
        return rssModule;
    }

    public void setRssModule(boolean rssModule) {
        this.rssModule = rssModule;
    }

    public boolean isPollModule() {
        return pollModule;
    }

    public void setPollModule(boolean pollModule) {
        this.pollModule = pollModule;
    }

    public boolean isGalleryModule() {
        return galleryModule;
    }

    public void setGalleryModule(boolean galleryModule) {
        this.galleryModule = galleryModule;
    }

    public boolean isQuestModule() {
        return questModule;
    }

    public void setQuestModule(boolean questModule) {
        this.questModule = questModule;
    }
}
