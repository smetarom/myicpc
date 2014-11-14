package com.myicpc.controller.admin;

import com.google.common.collect.Lists;
import com.google.gson.JsonParser;
import com.myicpc.commons.utils.WebServiceUtils;
import com.myicpc.controller.GeneralAdminController;
import com.myicpc.enums.FeedRunStrategyType;
import com.myicpc.model.contest.Contest;
import com.myicpc.repository.contest.ContestRepository;
import com.myicpc.service.contest.CMService;
import com.myicpc.service.contest.ContestService;
import com.myicpc.service.settings.GlobalSettingsService;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
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
    private CMService cmService;

    @Autowired
    private ContestService contestService;

    @Autowired
    private ContestRepository contestRepository;

    @RequestMapping(value = "/private/contests", method = RequestMethod.GET)
    public String contests(Model model) {
        Sort sort = new Sort(Sort.Direction.DESC, "startTime");
        List<Contest> contests = contestRepository.findAll(sort);

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
        contest.getContestSettings().setShowTeamNames(true);

        return createContest(1, contest, model);
    }

    @RequestMapping(value = "/private/contest/create/{step}", method = RequestMethod.GET)
    public String createContest(@PathVariable Integer step, @ModelAttribute Contest contest, Model model) {
        if (contest == null) {
            return "redirect:/private/contest/create";
        }
        List<ImmutablePair<String, String>> steps = getWizardMenuItems();
        model.addAttribute("contest", contest);
        model.addAttribute("headline", getMessage("contestAdmin.create.title"));
        model.addAttribute("steps", steps);
        model.addAttribute("currentStep", step);
        model.addAttribute("scoreboardStrategies", Arrays.asList(FeedRunStrategyType.values()));
        model.addAttribute("formAction", step == steps.size() ? "/private/contest/create" : "/private/contest/nextStep");
        return "private/contest/editContest";
    }

    @RequestMapping(value = "/private/contest/create", method = RequestMethod.POST)
    public String createContest(@Valid @ModelAttribute Contest contest, RedirectAttributes redirectAttributes, Model model, BindingResult result) {
        if (result.hasErrors()) {
            List<ImmutablePair<String, String>> steps = getWizardMenuItems();
            model.addAttribute("contest", contest);
            model.addAttribute("headline", getMessage("contestAdmin.create.title"));
            model.addAttribute("steps", steps);
            model.addAttribute("currentStep", steps.size());
            model.addAttribute("formAction", "/private/contest/create");
            model.addAttribute("defaultMapConfig", globalSettingsService.getGlobalSettings().getDefaultMapConfig());
            return "private/contest/editContest";
        }

        contestRepository.save(contest);
        getMessage("contestAdmin.create.success", contest.getName());
        return "redirect:/private/home";
    }

    @RequestMapping(value = "/private/contest/nextStep", method = RequestMethod.POST)
    public String nextStep(@ModelAttribute Contest contest, @RequestParam Integer currentStep) {
        return "redirect:/private/contest/create/" + (currentStep + 1);
    }

    @RequestMapping(value = "/private/contest/checkWebService", method = RequestMethod.GET)
    @ResponseBody
    public String checkWebService(@ModelAttribute Contest contest, @RequestParam String contestCode, @RequestParam String wsToken,
                                  RedirectAttributes redirectAttributes) throws IOException {
        contest.setCode(contestCode);
        contest.getWebServiceSettings().setWsCMToken(wsToken);
        String result = "";
        try {
            // TODO renew web service check, maybe create simple to check in CM4
            result = cmService.getUniversitiesFromCM(contest);
            new JsonParser().parse(result);
            return getMessage("contestAdmin.wsCheck.passed");
        } catch (Exception ex) {
            String error = "";
            int startFrom = result.indexOf("<message>");
            int endIn = result.indexOf("</message>");
            if (startFrom > 0 && endIn > 0) {
                error = result.substring(startFrom, endIn);
            }
            return getMessage("contestAdmin.wsCheck.failed", error);
        }
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

    @RequestMapping(value = "/private/contest/map/team-coordinates", method = RequestMethod.GET)
    @ResponseBody
    public String teamMapCoordinates(@ModelAttribute Contest contest, final Model model) throws IOException {
        return cmService.getTeamCoordinatesCM(contest);
    }

    protected List<ImmutablePair<String, String>> getWizardMenuItems() {
        List<ImmutablePair<String, String>> items = Lists.newArrayList();
        items.add(new ImmutablePair<String, String>("Initialize", getMessage("contestAdmin.wizard.stepInit")));
        items.add(new ImmutablePair<String, String>("ContestInfo", getMessage("contestAdmin.wizard.stepContestInfo")));
        items.add(new ImmutablePair<String, String>("EventFeed", getMessage("contestAdmin.wizard.stepEventFeed")));
        items.add(new ImmutablePair<String, String>("TeamEdit", getMessage("contestAdmin.wizard.stepTeamEdit")));
        items.add(new ImmutablePair<String, String>("TeamPictures", getMessage("contestAdmin.wizard.stepTeamPictures")));
        items.add(new ImmutablePair<String, String>("Map", getMessage("contestAdmin.wizard.stepMap")));
        items.add(new ImmutablePair<String, String>("Twitter", getMessage("contestAdmin.wizard.stepTwitter")));
        items.add(new ImmutablePair<String, String>("Instagram", getMessage("contestAdmin.wizard.stepInstagram")));
        items.add(new ImmutablePair<String, String>("Vine", getMessage("contestAdmin.wizard.stepVine")));
        items.add(new ImmutablePair<String, String>("Youtube", getMessage("contestAdmin.wizard.stepYoutube")));
        items.add(new ImmutablePair<String, String>("Summary", getMessage("contestAdmin.wizard.stepSummary")));

        return items;
    }

    @InitBinder("contest")
    protected void initEventBinder(WebDataBinder binder) {
        bindDateTimeFormatAllowEmpty(binder);
    }
}
