package com.myicpc.controller.scoreboard.admin;

import com.myicpc.controller.GeneralAdminController;
import com.myicpc.model.contest.Contest;
import com.myicpc.service.scoreboard.eventFeed.ControlFeedService;
import com.myicpc.service.scoreboard.exception.EventFeedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

/**
 * Controller for event feed management
 *
 * @author Roman Smetana
 */
@Controller
public class EventFeedController extends GeneralAdminController {

    @Autowired
    private ControlFeedService controlFeedService;

    @RequestMapping(value = "/private/{contestCode}/feed/status", method = RequestMethod.GET)
    public String feedStatus(@PathVariable final String contestCode, Model model) {
        Contest contest = getContest(contestCode, model);

        boolean isFeedRunning = controlFeedService.isEventFeedProcessorRunning(contest);

        model.addAttribute("isFeedRunning", isFeedRunning);
        return "private/contest/fragment/eventFeedControlPanel";
    }

    @RequestMapping(value = "/private/{contestCode}/feed/restart", method = RequestMethod.POST)
    public String restartFeed(@PathVariable final String contestCode, final RedirectAttributes redirectAttributes) {
        Contest contest = getContest(contestCode, null);

        try {
            controlFeedService.restartEventFeed(contest);
            successMessage(redirectAttributes, "admin.panel.feed.reset.success");
        } catch (EventFeedException ex) {
            errorMessage(redirectAttributes, ex);
        }

        return "redirect:/private"+ getContestURL(contestCode) + "/home";
    }

    @RequestMapping(value = "/private/{contestCode}/feed/resume", method = RequestMethod.POST)
    public String resumeFeed(@PathVariable final String contestCode, final RedirectAttributes redirectAttributes) {
        Contest contest = getContest(contestCode, null);

        try {
            controlFeedService.resumeEventFeed(contest);
            successMessage(redirectAttributes, "admin.panel.feed.resume.success");
        } catch (EventFeedException ex) {
            errorMessage(redirectAttributes, ex);
        }

        return "redirect:/private"+ getContestURL(contestCode) + "/home";
    }

    @RequestMapping(value = "/private/{contestCode}/feed/stop", method = RequestMethod.POST)
    public String stopFeed(@PathVariable final String contestCode, final RedirectAttributes redirectAttributes) {
        Contest contest = getContest(contestCode, null);

        controlFeedService.stopEventFeed(contest);
        successMessage(redirectAttributes, "admin.panel.feed.stop.success");

        return "redirect:/private"+ getContestURL(contestCode) + "/home";
    }

    @RequestMapping(value = "/private/{contestCode}/feed/clear", method = RequestMethod.POST)
    public String clearFeed( @PathVariable final String contestCode, final RedirectAttributes redirectAttributes) {
        Contest contest = getContest(contestCode, null);

        try {
            controlFeedService.clearEventFeed(contest);
        } catch (EventFeedException ex) {
            errorMessage(redirectAttributes, ex);
        }

        return "redirect:/private"+ getContestURL(contestCode) + "/home";
    }

    @RequestMapping(value = "/private/{contestCode}/feed/upload", method = RequestMethod.POST)
    public String uploadFile(@RequestParam MultipartFile eventFeedFile, @PathVariable final String contestCode, final RedirectAttributes redirectAttributes) {
        Contest contest = getContest(contestCode, null);

        try {
            controlFeedService.uploadEventFeed(eventFeedFile.getInputStream(), contest);
        } catch (EventFeedException | IOException e) {
            errorMessage(redirectAttributes, e);
        }

        return "redirect:/private"+ getContestURL(contestCode) + "/home";
    }
}
