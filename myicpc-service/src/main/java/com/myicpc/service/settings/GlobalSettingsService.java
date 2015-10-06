package com.myicpc.service.settings;

import com.myicpc.model.Globals;
import com.myicpc.repository.GlobalsRepository;
import com.myicpc.repository.security.SystemUserRoleRepository;
import com.myicpc.service.dto.GlobalSettings;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Service responsible for global settings
 *
 * Global settings is MyICPC settings, which is shared between all contests
 *
 * @author Roman Smetana
 */
@Service
public class GlobalSettingsService {
    private static final Logger logger = LoggerFactory.getLogger(GlobalSettingsService.class);

    @Autowired
    private GlobalsRepository globalsRepository;

    @Autowired
    private SystemUserRoleRepository systemUserRoleRepository;

    /**
     * Checks, if the install mode is available
     *
     * Install mode is available only if there is no admin account
     *
     * @return is the install mode available
     */
    public boolean isInstallPhaseEnabled() {
        Long adminCount = systemUserRoleRepository.countAdminUsers();

        return adminCount == null || Long.valueOf(0).equals(adminCount);
    }

    /**
     * Loads global settings
     *
     * @return global settings
     */
    public GlobalSettings getGlobalSettings() {
        return loadGlobalSettings();
    }

    /**
     * Merges {@code globalSettings} with default predefined values
     *
     * It uses default values for all unset values
     *
     * @param globalSettings global settings to be updated
     */
    public void mergeDefaultGlobalSettings(final GlobalSettings globalSettings) {
        Properties conf = new Properties();
        try (InputStream settingsInputStream = GlobalSettingsService.class.getClassLoader().getResourceAsStream("defaultSettings.properties")) {
            conf.load(settingsInputStream);
            globalSettings.setDefaultMapConfig(conf.getProperty("mapConfig"));
            globalSettings.setUniversityLogosUrl(conf.getProperty("universityLogosURL"));
            if (StringUtils.isEmpty(globalSettings.getTeamPicturesUrl())) {
                globalSettings.setTeamPicturesUrl(conf.getProperty("teamPicturesURL"));
            }
            if (StringUtils.isEmpty(globalSettings.getContestManagementSystemUrl())) {
                globalSettings.setContestManagementSystemUrl(conf.getProperty("contestManagementSystemUrl"));
            }
        } catch (IOException e) {
            logger.warn("Cannot find configuration file defaultSettings.properties");
        }
    }

    private GlobalSettings loadGlobalSettings() {
        GlobalSettings globalSettings = new GlobalSettings();
        List<Globals> globals = globalsRepository.findAll();
        for (Globals global : globals) {
            try {
                Globals.GlobalsColumn column = Globals.GlobalsColumn.valueOf(global.getName());
                if (column.isLongText()) {
                    globalSettings.addSettings(column, global.getText());
                } else {
                    globalSettings.addSettings(column, global.getValue());
                }
            } catch (IllegalArgumentException ex) {
                logger.warn("Unknown value '" + global.getName() + "' of the column from Globals table in database");
            }
        }
        return globalSettings;
    }

    /**
     * Persists global settings
     *
     * @param globalSettings global settings to be persisted
     */
    public void saveGlobalSettings(GlobalSettings globalSettings) {
        for (Map.Entry<Globals.GlobalsColumn, String> entry : globalSettings.getSettingsMap().entrySet()) {
            saveProperty(entry.getKey(), entry.getValue());
        }
    }

    private void saveProperty(Globals.GlobalsColumn name, String value) {
        Globals globals = globalsRepository.findByName(name.toString());
        if (globals == null) {
            globals = new Globals(name);
        }
        if (name.isLongText()) {
            globals.setText(value);
        } else {
            globals.setValue(value);
        }
        globalsRepository.save(globals);
    }
}
