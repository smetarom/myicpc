package com.myicpc.model.contest;

import com.myicpc.model.IdGeneratedObject;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;

/**
 * @author Roman Smetana
 */
@Entity
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "ModuleConfiguration_id_seq")
public class ModuleConfiguration extends IdGeneratedObject {
    private boolean mapModule;
    private boolean codeInsightModule;

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
}
