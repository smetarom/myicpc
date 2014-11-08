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
        String mapConfig = "[{width: 700, ratio: 0.55, scale: 115, translate: [ 340,210 ], areas: {'EU': {\"scale\":220,\"translate\":[\"50\",\"420\"]}, 'AS': {\"scale\":260,\"translate\":[\"-80\",\"310\"]}, 'AF': {\"scale\":250,\"translate\":[\"300\",\"200\"]}, 'NA' : {\"scale\":320,\"translate\":[\"950\",\"500\"]}, 'SA' : {\"scale\":210,\"translate\":[\"500\",\"130\"]}, 'AU' : {\"scale\":400,\"translate\":[\"-700\",\"-10\"]}}},{width: 900, scale: 145, translate: [ 440,360 ], areas: {'EU': {\"scale\":280,\"translate\":[\"50\",\"530\"]}, 'AS': {\"scale\":325,\"translate\":[\"-80\",\"390\"]}, 'AF': {\"scale\":310,\"translate\":[\"410\",\"250\"]}, 'NA' : {\"scale\":370,\"translate\":[\"1200\",\"580\"]}, 'SA' : {\"scale\":250,\"translate\":[\"600\",\"150\"]}, 'AU' : {\"scale\":450,\"translate\":[\"-700\",\"-10\"]}}},{width: 1300, ratio: 0.6, scale: 210, translate: [ 620,480 ], circleSize: 8, areas: {'EU': {\"scale\":410,\"translate\":[\"80\",\"780\"]}, 'AS': {\"scale\":450,\"translate\":[\"-80\",\"540\"]}, 'AF': {\"scale\":420,\"translate\":[\"580\",\"340\"]}, 'NA' : {\"scale\":500,\"translate\":[\"1550\",\"780\"]}, 'SA' : {\"scale\":370,\"translate\":[\"900\",\"220\"]}, 'AU' : {\"scale\":640,\"translate\":[\"-900\",\"-10\"]}}}];";
        GlobalSettings gs = new GlobalSettings();
        return gs;
    }

    public void mergeDefaultGlobalSettings(final GlobalSettings globalSettings) {
        Properties conf = new Properties();
        try (InputStream settingsInputStream = GlobalSettingsService.class.getClassLoader().getResourceAsStream("defaultSettings.properties")) {
            conf.load(settingsInputStream);
            globalSettings.setDefaultMapConfig(conf.getProperty("mapConfig"));
            globalSettings.setUniversityLogosURL(conf.getProperty("universityLogosURL"));
        } catch (IOException e) {
            logger.warn("Cannot find configuration file defaultSettings.properties");
        }
    }

    public void loadGlobalSettings() {
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
