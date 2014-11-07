package com.myicpc.controller.admin;

import com.google.common.collect.Lists;
import com.myicpc.controller.GeneralAdminController;
import com.myicpc.model.security.SystemUser;
import com.myicpc.security.config.UserRoleEnum;
import com.myicpc.service.dto.GlobalSettings;
import com.myicpc.service.settings.GlobalSettingsService;
import com.myicpc.service.user.SystemUserService;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @author Roman Smetana
 */
@Controller
@SessionAttributes({"adminUser", "globalSettings"})
public class InstallAdminController extends GeneralAdminController {
    @Autowired
    private SystemUserService systemUserService;

    @Autowired
    private GlobalSettingsService globalSettingsService;

    @RequestMapping(value = {"/private/install", "/private/install/admin"}, method = RequestMethod.GET)
    public String installAdminUser(final Model model) {
        if (!globalSettingsService.isInstallPhaseEnabled()) {
            return "redirect:/private/install/error";
        }
        List<ImmutablePair<String, String>> steps = getWizardMenuItems();
        SystemUser adminUser = new SystemUser();

        model.addAttribute("entity", "adminUser");
        model.addAttribute("adminUser", adminUser);
        model.addAttribute("steps", steps);
        model.addAttribute("currentStep", 1);
        model.addAttribute("formAction", "/private/install/admin");
        return "/private/install/newInstall";
    }

    @RequestMapping(value = "/private/install/admin", method = RequestMethod.POST)
    public String processAdminUser(@Valid @ModelAttribute("adminUser") SystemUser adminUser, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        if (!systemUserService.checkPasswordVerification(adminUser)) {
            result.rejectValue("passwordCheck", "user.passwordCheck.error");
        }
        if (result.hasErrors()) {
            List<ImmutablePair<String, String>> steps = getWizardMenuItems();
            model.addAttribute("entity", "adminUser");
            model.addAttribute("adminUser", adminUser);
            model.addAttribute("steps", steps);
            model.addAttribute("currentStep", 1);
            model.addAttribute("formAction", "/private/install/admin");
            return "/private/install/newInstall";
        }
        return "redirect:/private/install/settings";
    }

    @RequestMapping(value = "/private/install/settings", method = RequestMethod.GET)
    public String installGlobalSettings(@ModelAttribute("adminUser") SystemUser adminUser, final Model model) {
        if (!globalSettingsService.isInstallPhaseEnabled()) {
            return "redirect:/private/install/error";
        }
        List<ImmutablePair<String, String>> steps = getWizardMenuItems();
        GlobalSettings globalSettings = new GlobalSettings();
        globalSettings.setAdminEmail(adminUser.getUsername());

        model.addAttribute("entity", "globalSettings");
        model.addAttribute("globalSettings", globalSettings);
        model.addAttribute("steps", steps);
        model.addAttribute("currentStep", 2);
        model.addAttribute("formAction", "/private/install/settings");
        return "/private/install/newInstall";
    }

    @RequestMapping(value = "/private/install/settings", method = RequestMethod.POST)
    public String processGlobalSettings(@Valid @ModelAttribute("globalSettings") GlobalSettings globalSettings, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        // TODO validate
        return "redirect:/private/install/summary";
    }

    @RequestMapping(value = "/private/install/summary", method = RequestMethod.GET)
    public String installSummary(@ModelAttribute("adminUser") final SystemUser adminUser, final Model model) {
        if (!globalSettingsService.isInstallPhaseEnabled()) {
            return "redirect:/private/install/error";
        }
        List<ImmutablePair<String, String>> steps = getWizardMenuItems();
        model.addAttribute("adminUser", adminUser);
        model.addAttribute("steps", steps);
        model.addAttribute("currentStep", 3);
        model.addAttribute("formAction", "/private/install/summary");
        return "/private/install/newInstall";
    }

    @Transactional
    @RequestMapping(value = "/private/install/summary", method = RequestMethod.POST)
    public String processSummary(@ModelAttribute("adminUser") SystemUser adminUser, @ModelAttribute("globalSettings") GlobalSettings globalSettings, RedirectAttributes redirectAttributes) {
        adminUser.getStringRoles().add(UserRoleEnum.ADMIN.toString());
        adminUser.setEnabled(true);
        adminUser.setPassword(systemUserService.hashPassword(adminUser.getPassword()));
        systemUserService.mergeUser(adminUser);

        globalSettingsService.saveGlobalSettings(globalSettings);

        return "redirect:/private/welcome/after-installation";
    }

    @RequestMapping(value = "/private/install/error", method = RequestMethod.GET)
    public String installSummary(final Model model) {
        return "/private/install/errorInstall";
    }

    @RequestMapping(value = "/private/welcome/after-installation", method = RequestMethod.GET)
    public String afterInstallWelcome(final Model model) {
        return "/private/install/welcome";
    }

    protected List<ImmutablePair<String, String>> getWizardMenuItems() {
        List<ImmutablePair<String, String>> items = Lists.newArrayList();
        items.add(new ImmutablePair<String, String>("Admin", getMessage("installAdmin.wizard.setupAdmin")));
        items.add(new ImmutablePair<String, String>("GlobalSettings", getMessage("installAdmin.wizard.globalSettings")));
        items.add(new ImmutablePair<String, String>("Summary", getMessage("installAdmin.wizard.summary")));

        return items;
    }
}
