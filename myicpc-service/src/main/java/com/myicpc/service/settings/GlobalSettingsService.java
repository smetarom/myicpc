package com.myicpc.service.settings;

import com.myicpc.model.Globals;
import com.myicpc.repository.GlobalsRepository;
import com.myicpc.repository.security.SystemUserRoleRepository;
import com.myicpc.service.dto.GlobalSettings;
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
 * @author Roman Smetana
 */
@Service
public class GlobalSettingsService {
    private static final Logger logger = LoggerFactory.getLogger(GlobalSettingsService.class);

    @Autowired
    private GlobalsRepository globalsRepository;

    @Autowired
    private SystemUserRoleRepository systemUserRoleRepository;

    public boolean isInstallPhaseEnabled() {
        Long adminCount = systemUserRoleRepository.countAdminUsers();

        return adminCount == null || Long.valueOf(0).equals(adminCount);
    }

    public GlobalSettings getGlobalSettings() {
        return loadGlobalSettings();
    }

    public void mergeDefaultGlobalSettings(final GlobalSettings globalSettings) {
        Properties conf = new Properties();
        try (InputStream settingsInputStream = GlobalSettingsService.class.getClassLoader().getResourceAsStream("defaultSettings.properties")) {
            conf.load(settingsInputStream);
            globalSettings.setDefaultMapConfig(conf.getProperty("mapConfig"));
            globalSettings.setUniversityLogosUrl(conf.getProperty("universityLogosURL"));
        } catch (IOException e) {
            logger.warn("Cannot find configuration file defaultSettings.properties");
        }
    }

    public GlobalSettings loadGlobalSettings() {
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

    public void saveGlobalSettings(GlobalSettings globalSettings) {
        for (Map.Entry<Globals.GlobalsColumn, String> entry : globalSettings.getSettingsMap().entrySet()) {
            saveProperty(entry.getKey(), entry.getValue());
        }
    }

    protected void saveProperty(Globals.GlobalsColumn name, String value) {
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
