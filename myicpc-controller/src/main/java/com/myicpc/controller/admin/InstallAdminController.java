package com.myicpc.controller.admin;

import com.google.common.collect.Lists;
import com.myicpc.controller.GeneralAdminController;
import com.myicpc.model.security.SystemUser;
import com.myicpc.service.dto.GlobalSettings;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
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
    @RequestMapping(value = {"/private/install", "/private/install/admin"}, method = RequestMethod.GET)
    public String installAdminUser(final Model model) {
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
    public String processAdminUser(@Valid @ModelAttribute SystemUser adminUser, RedirectAttributes redirectAttributes, Model model, BindingResult result) {
        if (result.hasErrors()) {
            List<ImmutablePair<String, String>> steps = getWizardMenuItems();
            model.addAttribute("steps", steps);
            model.addAttribute("currentStep", 1);
            model.addAttribute("formAction", "/private/install/admin");
            return "/private/install/newInstall";
        }
        return "redirect:/private/install/settings";
    }

    @RequestMapping(value = "/private/install/settings", method = RequestMethod.GET)
    public String installGlobalSettings(@ModelAttribute SystemUser adminUser, final Model model) {
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
    public String processGlobalSettings(@Valid @ModelAttribute GlobalSettings globalSettings, RedirectAttributes redirectAttributes, Model model, BindingResult result) {
        // TODO validate
        return "redirect:/private/install/summary";
    }

    @RequestMapping(value = "/private/install/summary", method = RequestMethod.GET)
    public String installSummary(final Model model) {
        List<ImmutablePair<String, String>> steps = getWizardMenuItems();
        GlobalSettings globalSettings = new GlobalSettings();

        model.addAttribute("entity", "globalSettings");
        model.addAttribute("globalSettings", globalSettings);
        model.addAttribute("steps", steps);
        model.addAttribute("currentStep", 3);
        model.addAttribute("formAction", "/private/install/settings");
        return "/private/install/newInstall";
    }

    @RequestMapping(value = "/private/install/summary", method = RequestMethod.POST)
    public String processSummary(@Valid @ModelAttribute SystemUser adminUser, @Valid @ModelAttribute GlobalSettings globalSettings, RedirectAttributes redirectAttributes) {
        // TODO validate
        return "redirect:/private/install/summary";
    }

    protected List<ImmutablePair<String, String>> getWizardMenuItems() {
        List<ImmutablePair<String, String>> items = Lists.newArrayList();
        items.add(new ImmutablePair<String, String>("Admin", getMessage("installAdmin.wizard.setupAdmin")));
        items.add(new ImmutablePair<String, String>("GlobalSettings", getMessage("installAdmin.wizard.globalSettings")));
        items.add(new ImmutablePair<String, String>("Summary", getMessage("installAdmin.wizard.summary")));

        return items;
    }
}
