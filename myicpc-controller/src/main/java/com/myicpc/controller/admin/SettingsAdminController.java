package com.myicpc.controller.admin;

import com.myicpc.controller.GeneralAdminController;
import com.myicpc.model.security.SystemUser;
import com.myicpc.service.dto.GlobalSettings;
import com.myicpc.service.settings.GlobalSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author Roman Smetana
 */
@Controller
@SessionAttributes("globalSettings")
public class SettingsAdminController extends GeneralAdminController {
    @Autowired
    private GlobalSettingsService globalSettingsService;

    @RequestMapping(value = "/private/settings", method = RequestMethod.GET)
    public String globalSettings(final Model model) {
        GlobalSettings globalSettings = globalSettingsService.getGlobalSettings();

        model.addAttribute("globalSettings", globalSettings);
        return "/private/settings/settings";
    }

    @RequestMapping(value = "/private/settings", method = RequestMethod.POST)
    public String saveGlobalSettings(@ModelAttribute("globalSettings") GlobalSettings globalSettings, RedirectAttributes redirectAttributes) {

        globalSettingsService.saveGlobalSettings(globalSettings);
        successMessage(redirectAttributes, "save.success");
        return "redirect:/private/settings";
    }
}
