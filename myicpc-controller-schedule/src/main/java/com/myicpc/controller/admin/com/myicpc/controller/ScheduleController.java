package com.myicpc.controller.admin.com.myicpc.controller;

import com.myicpc.controller.GeneralController;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.schedule.Event;
import com.myicpc.model.schedule.Location;
import com.myicpc.repository.schedule.EventRepository;
import com.myicpc.repository.schedule.LocationRepository;
import com.myicpc.service.schedule.ScheduleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.site.SitePreference;
import org.springframework.mobile.device.site.SitePreferenceUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author Roman Smetana
 */
@Controller
public class ScheduleController extends GeneralController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private LocationRepository locationRepository;

    @RequestMapping(value = "/{contestCode}/schedule", method = RequestMethod.GET)
    public String schedule(@PathVariable String contestCode, Model model,
                           @CookieValue(value = "scheduleRoles", required = false) String scheduleRoles, HttpServletRequest request, HttpServletResponse response) {
        Contest contest = getContest(contestCode, model);

        if (StringUtils.isEmpty(scheduleRoles)) {
            // TODO
//            String param = eventService.getAllScheduleRoles(contest);
//            GlobalUtils.setCookie(request, response, "scheduleRoles", param);
        }

        // TODO replace with new Date()
        Calendar calendar = new GregorianCalendar(2014, 1, 1);

        model.addAttribute("schedule", scheduleService.getEntireContestSchedule(contest));
        model.addAttribute("pageHeadline", getMessage("schedule.title"));
        model.addAttribute("pageTitle", getMessage("nav.schedule"));
        model.addAttribute("sideMenuActive", "schedule");
        return "schedule/schedule";
    }

    @RequestMapping(value = "/{contestCode}/my-schedule", method = RequestMethod.GET)
    public String mySchedule(@PathVariable String contestCode, Model model,
                             @CookieValue(value = "scheduleRoles", required = false) String scheduleRoles, RedirectAttributes redirectAttributes,
                             HttpServletRequest request, HttpServletResponse response) {
        Contest contest = getContest(contestCode, model);

        if (StringUtils.isEmpty(scheduleRoles)) {
            // TODO
//            scheduleRoles = eventService.getAllScheduleRoles(contest);
//            GlobalUtils.setCookie(request, response, "scheduleRoles", scheduleRoles);
//            if (StringUtils.isEmpty(scheduleRoles)) {
//                return "redirect:" + getContestURL(contestCode) + "/schedule";
//            }
        }

        // TODO replace with new Date()
        Calendar calendar = new GregorianCalendar(2014, 1, 1);

        String[] roleIds = scheduleRoles.split(",");
        model.addAttribute("schedule", scheduleService.getMyCurrentSchedule(roleIds, calendar.getTime()));
        model.addAttribute("pageHeadline", getMessage("myschedule.title"));
        model.addAttribute("pageTitle", getMessage("nav.myschedule"));
        model.addAttribute("sideMenuActive", "schedule");
        return "schedule/schedule";
    }

    @RequestMapping(value = "/{contestCode}/schedule/event/{eventId}", method = RequestMethod.GET)
    public String eventDetail(@PathVariable String contestCode, @PathVariable String eventId, Model model, RedirectAttributes redirectAttributes,
                              HttpServletRequest request, HttpServletResponse response) {
        Contest contest = getContest(contestCode, model);

        SitePreference sitePreference = SitePreferenceUtils.getCurrentSitePreference(request);
        if (!sitePreference.isMobile()) {
            return "redirect:" + getContestURL(contestCode) + "/schedule";
        }

        model.addAttribute("hideTitle", true);
        return _eventDetail(contest, "schedule/eventDetail", "redirect:" + getContestURL(contestCode) + "/schedule", eventId, model,
                redirectAttributes);

    }

    /**
     * Show a detail about event
     *
     * @param eventId
     *            event ID
     * @param model
     * @param redirectAttributes
     * @return view
     */
    @RequestMapping(value = "{contestCode}/schedule/ajax/event/{eventId}", method = RequestMethod.GET)
    public String eventAjaxDetail(@PathVariable String contestCode, @PathVariable String eventId, Model model, RedirectAttributes redirectAttributes) {
        Contest contest = getContest(contestCode, model);
        return _eventDetail(contest, "schedule/eventDetail", "", eventId, model, redirectAttributes);
    }

    protected String _eventDetail(Contest contest, String view, String redirectView, String eventId, Model model,
                                  RedirectAttributes redirectAttributes) {
        Event event;
        try {
            Long id = Long.parseLong(eventId);
            event = eventRepository.findOne(id);
        } catch (Exception ex) {
            event = eventRepository.findByCode(eventId);
        }

        if (event == null) {
            errorMessage(redirectAttributes, "scheduleAdmin.noResult");
            return redirectView;
        }

        model.addAttribute("event", event);
        model.addAttribute("hashTags", "#" + event.getHashtag() + " #" + contest.getHashtag());
        model.addAttribute("hashTagsURL", event.getHashtag() + "," + contest.getHashtag());
        model.addAttribute("contestYear", contest.getContestSettings().getYear());
        model.addAttribute("pageTitle", event.getName());
        // TODO
//        model.addAttribute("picasaUsername", WebServiceUtils.getShowPhotosUsername());
        model.addAttribute("sideMenuActive", "schedule");
        return view;
    }

    @RequestMapping(value = "/{contestCode}/venues", method = RequestMethod.GET)
    public String venues(@PathVariable String contestCode, Model model, HttpServletRequest request, HttpServletResponse response) {
        Contest contest = getContest(contestCode, model);

        model.addAttribute("venues", locationRepository.findByContestOrderByNameAsc(contest));
        model.addAttribute("pageTitle", getMessage("nav.venues"));
        model.addAttribute("sideMenuActive", "schedule");

        return "schedule/venues";
    }

    @RequestMapping(value = "/{contestCode}/venue/{venueId}", method = RequestMethod.GET)
    public String venueDetail(@PathVariable String contestCode, @PathVariable String venueId, Model model, HttpServletRequest request,
                              HttpServletResponse response) {
        getContest(contestCode, model);

        SitePreference sitePreference = SitePreferenceUtils.getCurrentSitePreference(request);
        if (!sitePreference.isMobile()) {
            return "redirect:" + getContestURL(contestCode) + "/venues";
        }


        _venueDetail(venueId, model);
        model.addAttribute("sideMenuActive", "schedule");

        return "schedule/venueDetail";
    }

    @RequestMapping(value = "/{contestCode}/venue/ajax/{venueId}", method = RequestMethod.GET)
    public String venueDetailAjax(@PathVariable String contestCode, @PathVariable String venueId, Model model, HttpServletRequest request,
                                  HttpServletResponse response) {
        getContest(contestCode, model);

        _venueDetail(venueId, model);

        return "schedule/venueDetail";
    }

    protected void _venueDetail(String venueId, Model model) {
        Location location;
        try {
            Long id = Long.parseLong(venueId);
            location = locationRepository.findOne(id);
        } catch (Exception ex) {
            location = locationRepository.findByCode(venueId);
        }

        model.addAttribute("venue", location);
        model.addAttribute("schedule", scheduleService.getScheduleEventsInLocation(location));
        model.addAttribute("pageTitle", location.getName());
    }
}
