package com.myicpc.controller.admin;

import com.google.common.collect.Lists;
import com.myicpc.controller.GeneralAdminController;
import com.myicpc.dto.TranslationDto;
import com.myicpc.enums.FeedRunStrategyType;
import com.myicpc.model.contest.Contest;
import com.myicpc.repository.contest.ContestRepository;
import com.myicpc.service.contest.ContestService;
import com.myicpc.service.exception.WebServiceException;
import com.myicpc.service.settings.GlobalSettingsService;
import com.myicpc.service.webSevice.ContestWSService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

@Controller
@SessionAttributes("contest")
public class ContestAdminController extends GeneralAdminController {
    @Autowired
    private GlobalSettingsService globalSettingsService;

    @Autowired
    private ContestService contestService;

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private ContestWSService contestWSService;

    @RequestMapping(value = "/private/contests", method = RequestMethod.GET)
    public String contests(Model model) {
        Sort sort = new Sort(Sort.Direction.DESC, "startTime");
        List<Contest> contests = contestService.getContestsSecured(sort);

        String[] relationship = new String[] {
                "ROLE_ADMIN > ROLE_MANAGER",
                "ROLE_MANAGER > ROLE_USER"
        };
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy(StringUtils.join(relationship, " "));

        model.addAttribute("contests", contests);
        return "private/contest/contestList";
    }

    @RequestMapping(value = { "/private/{contestCode}", "/private/{contestCode}/home" }, method = RequestMethod.GET)
    public String contestHome(@PathVariable String contestCode, final Model model) {
        Contest contest = getContest(contestCode, model);

        return "/private/contest/contestHome";
    }

    @RequestMapping(value = "/private/{contestCode}/overview", method = RequestMethod.GET)
    public String contestOverview(@PathVariable String contestCode, final Model model) {
        Contest contest = getContest(contestCode, model);

        return "/private/contest/contestDetail";
    }

    @RequestMapping(value = "/private/contest/create", method = RequestMethod.GET)
    public String createContest(Model model) {
        Contest contest = new Contest();
        contest.getContestSettings().setYear(Calendar.getInstance().get(Calendar.YEAR));
        contest.setShowTeamNames(true);
        contest.getQuestConfiguration().setHashtagPrefix(getMessage("quest.hashtag.default"));
        contest.getQuestConfiguration().setContest(contest);

        return createContest(1, contest, model);
    }

    @RequestMapping(value = "/private/contest/create/{step}", method = RequestMethod.GET)
    public String createContest(@PathVariable Integer step, @ModelAttribute Contest contest, Model model) {
        if (contest == null) {
            return "redirect:/private/contest/create";
        }
        populateCreateContestModel(model, contest, step);
        return "private/contest/editContest";
    }

    @RequestMapping(value = "/private/contest/create", method = RequestMethod.POST)
    public String createContest(@Valid @ModelAttribute Contest contest, RedirectAttributes redirectAttributes, Model model, BindingResult result) {
        if (result.hasErrors()) {
            List<ImmutablePair<String, String>> steps = getWizardMenuItems(false);
            populateCreateContestModel(model, contest, steps.size());
            return "private/contest/editContest";
        }

        contestService.createContest(contest);
        getMessage("contestAdmin.create.success", contest.getName());
        return "redirect:/private/home";
    }

    @RequestMapping(value = "/private/contest/nextStep", method = RequestMethod.POST)
    public String nextStep(@ModelAttribute Contest contest, @RequestParam Integer currentStep) {
        return "redirect:/private/contest/create/" + (currentStep + 1);
    }

    @RequestMapping(value = "/private/contest/checkWebService", method = RequestMethod.GET)
    @ResponseBody
    public String checkWebService(@ModelAttribute Contest contest, @RequestParam String contestCode, @RequestParam String wsToken) {
        contest.setCode(contestCode);
        contest.getWebServiceSettings().setWsCMToken(wsToken);
        try {
            TranslationDto translationDto = contestWSService.pingCMContest(contest);
            return getMessageWithDefault(translationDto.getCode(), translationDto.getText());
        } catch (Exception ex) {
            return getMessage("contestAdmin.wsCheck.failed");
        }
    }

    @RequestMapping(value = "/private/contest/cm-contest-details", method = RequestMethod.GET)
    @ResponseBody
    public String getContestDetails(@ModelAttribute Contest contest) throws IOException, WebServiceException {
        return contestWSService.getContestDetailFromCM(contest);
    }

    /**
     * Shows map configuration page
     *
     * @param model
     * @return view
     */
    @RequestMapping(value = "/private/{contestCode}/map/config", method = RequestMethod.GET)
    public String map(@ModelAttribute Contest contest, final Model model) {
        String config = null;
        if (contest.getMapConfiguration() != null) {
            config = contest.getMapConfiguration().getMapConfig();
        } else {
            config = globalSettingsService.getGlobalSettings().getDefaultMapConfig();
        }
        int[] widths = {700, 900, 1300};
        model.addAttribute("widths", widths);
        model.addAttribute("mapConfig", config);
        return "private/map/config";
    }

    @RequestMapping(value = "/private/{contestCode}/edit", method = RequestMethod.GET)
    public String editContest(@PathVariable final String contestCode, final Model model, RedirectAttributes redirectAttributes) {
        return editContest(contestCode, 1, model, redirectAttributes);
    }

    @RequestMapping(value = "/private/{contestCode}/edit/{step}", method = RequestMethod.GET)
    public String editContest(@PathVariable final String contestCode, @PathVariable Integer step, final Model model, RedirectAttributes redirectAttributes) {
        Contest contest = getContest(contestCode, model);
        if (contest == null) {
            errorMessage(redirectAttributes, "contestAdmin.list.noResult");
            return "redirect:/private/home";
        }
        populateEditContestModel(model, contest, step);
        return "private/contest/editContest";
    }

    @RequestMapping(value = "/private/contest/edit", method = RequestMethod.POST)
    public String editContest(@Valid @ModelAttribute Contest contest, @RequestParam Integer currentStep, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            populateEditContestModel(model, contest, currentStep);
            return "private/contest/editContest";
        }
        contestService.saveContest(contest);
        successMessage(redirectAttributes, "save.success");
        return "redirect:/private"+getContestURL(contest.getCode())+"/edit/" + currentStep;
    }

    @RequestMapping(value = "/private/{contestCode}/delete", method = RequestMethod.GET)
    public String deleteContest(@PathVariable final String contestCode, final Model model, RedirectAttributes redirectAttributes) {
        Contest contest = getContest(contestCode, model);
        if (contest == null) {
            errorMessage(redirectAttributes, "contestAdmin.list.noResult");
            return "redirect:/private/home";
        }
        model.addAttribute("contest", contest);
        return "private/contest/deleteContest";
    }

    @RequestMapping(value = "/private/{contestCode}/delete", method = RequestMethod.POST)
    public String deleteContestPOST(@PathVariable final String contestCode, RedirectAttributes redirectAttributes) {
        Contest contest = getContest(contestCode, null);
        contestService.deleteContest(contest);
        successMessage(redirectAttributes, "contestAdmin.delete.success");
        return "redirect:/private/home";
    }

    private void populateCreateContestModel(Model model, Contest contest, int currentStep) {
        List<ImmutablePair<String, String>> steps = getWizardMenuItems(false);
        model.addAttribute("headline", getMessage("contestAdmin.create.title"));
        model.addAttribute("formAction", currentStep == steps.size() ? "/private/contest/create" : "/private/contest/nextStep");
        model.addAttribute("cancelAction", "/private/home");
        model.addAttribute("stepURL", "/private/contest/create/");
        populateContestModel(model, contest, false, currentStep);
    }

    private void populateEditContestModel(Model model, Contest contest, int currentStep) {
        model.addAttribute("headline", getMessage("contestAdmin.edit.title", contest.getName()));
        model.addAttribute("formAction", "/private/contest/edit");
        model.addAttribute("cancelAction", "/private"+getContestURL(contest.getCode()));
        model.addAttribute("stepURL", "/private"+getContestURL(contest.getCode()) + "/edit/");
        populateContestModel(model, contest, true, currentStep);
    }

    private void populateContestModel(Model model, Contest contest, boolean editMode, int currentStep) {
        List<ImmutablePair<String, String>> steps = getWizardMenuItems(editMode);
        model.addAttribute("contest", contest);
        model.addAttribute("steps", steps);
        model.addAttribute("currentStep", currentStep);
        model.addAttribute("editMode", editMode);
        model.addAttribute("scoreboardStrategies", Arrays.asList(FeedRunStrategyType.values()));
        model.addAttribute("defaultMapConfig", globalSettingsService.getGlobalSettings().getDefaultMapConfig());
    }

    private List<ImmutablePair<String, String>> getWizardMenuItems(boolean editMode) {
        List<ImmutablePair<String, String>> items = Lists.newArrayList();
        items.add(new ImmutablePair<>("Initialize", getMessage("contestAdmin.wizard.stepInit")));
        items.add(new ImmutablePair<>("ContestInfo", getMessage("contestAdmin.wizard.stepContestInfo")));
        items.add(new ImmutablePair<>("EventFeed", getMessage("contestAdmin.wizard.stepEventFeed")));
        if (editMode) {
            items.add(new ImmutablePair<>("Modules", getMessage("contestAdmin.wizard.stepModules")));
        }
        items.add(new ImmutablePair<>("TeamEdit", getMessage("contestAdmin.wizard.stepTeamEdit")));
        items.add(new ImmutablePair<>("Quest", getMessage("contestAdmin.wizard.stepQuest")));
//        items.add(new ImmutablePair<>("TeamPictures", getMessage("contestAdmin.wizard.stepTeamPictures")));
//        items.add(new ImmutablePair<>("Map", getMessage("contestAdmin.wizard.stepMap")));
        items.add(new ImmutablePair<>("Twitter", getMessage("contestAdmin.wizard.stepTwitter")));
        items.add(new ImmutablePair<>("Instagram", getMessage("contestAdmin.wizard.stepInstagram")));
        items.add(new ImmutablePair<>("Vine", getMessage("contestAdmin.wizard.stepVine")));
        items.add(new ImmutablePair<>("Picasa", getMessage("contestAdmin.wizard.stepPicasa")));
//        items.add(new ImmutablePair<>("Youtube", getMessage("contestAdmin.wizard.stepYoutube")));
        if (!editMode) {
            items.add(new ImmutablePair<>("Summary", getMessage("contestAdmin.wizard.stepSummary")));
        }

        return items;
    }

    @InitBinder("contest")
    protected void initEventBinder(WebDataBinder binder) {
        bindDateTimeFormatAllowEmpty(binder);
    }
}
