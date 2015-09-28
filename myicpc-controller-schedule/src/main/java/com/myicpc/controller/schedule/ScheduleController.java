package com.myicpc.controller.schedule;

import com.myicpc.commons.utils.CookieUtils;
import com.myicpc.controller.GeneralController;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.schedule.Event;
import com.myicpc.model.schedule.EventRole;
import com.myicpc.model.schedule.Location;
import com.myicpc.repository.schedule.EventRoleRepository;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author Roman Smetana
 */
@Controller
public class ScheduleController extends GeneralController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private EventRoleRepository eventRoleRepository;

    @RequestMapping(value = "/{contestCode}/schedule", method = RequestMethod.GET)
    public String schedule(@PathVariable String contestCode, Model model,
                           @CookieValue(value = "scheduleRoles", required = false) String scheduleRoles,
                           HttpServletRequest request, HttpServletResponse response,
                           SitePreference sitePreference) {
        Contest contest = getContest(contestCode, model);

        if (StringUtils.isEmpty(scheduleRoles)) {
            String cookieParam = scheduleService.getAllScheduleRoles(contest);
            if (StringUtils.isNotEmpty(cookieParam)) {
                CookieUtils.setCookie(request, response, "scheduleRoles", cookieParam);
                model.addAttribute("showRoleDialog", true);
            }
        }

        model.addAttribute("schedule", scheduleService.getEntireContestSchedule(contest));
        model.addAttribute("pageHeadline", getMessage("schedule.title"));
        model.addAttribute("pageTitle", getMessage("nav.schedule"));
        model.addAttribute("sideMenuActive", "schedule");
        return resolveView("schedule/schedule", "schedule/schedule_mobile", sitePreference);
    }

    @RequestMapping(value = "/{contestCode}/my-schedule", method = RequestMethod.GET)
    public String mySchedule(@PathVariable String contestCode, Model model,
                             @CookieValue(value = "scheduleRoles", required = false) String scheduleRoles, RedirectAttributes redirectAttributes,
                             HttpServletRequest request, HttpServletResponse response,
                             SitePreference sitePreference) {
        Contest contest = getContest(contestCode, model);

        if (StringUtils.isEmpty(scheduleRoles)) {
            scheduleRoles = scheduleService.getAllScheduleRoles(contest);
            CookieUtils.setCookie(request, response, "scheduleRoles", scheduleRoles);
            if (StringUtils.isEmpty(scheduleRoles)) {
                return "redirect:" + getContestURL(contestCode) + "/schedule";
            }
            model.addAttribute("showRoleDialog", true);
        }

        // TODO replace with new Date()
        Calendar calendar = new GregorianCalendar(2014, 1, 1);

        String[] roleIds = scheduleRoles.split(",");
        model.addAttribute("schedule", scheduleService.getMyCurrentSchedule(roleIds, calendar.getTime(), contest));
        model.addAttribute("pageHeadline", getMessage("myschedule.title"));
        model.addAttribute("pageTitle", getMessage("nav.myschedule"));
        model.addAttribute("sideMenuActive", "schedule");
        return resolveView("schedule/schedule", "schedule/schedule_mobile", sitePreference);
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
     * @param eventId event ID
     * @param model
     * @param redirectAttributes
     * @return view
     */
    @RequestMapping(value = "{contestCode}/schedule/ajax/event/{eventId}", method = RequestMethod.GET)
    public String eventAjaxDetail(@PathVariable String contestCode, @PathVariable String eventId, Model model, RedirectAttributes redirectAttributes) {
        Contest contest = getContest(contestCode, model);
        return _eventDetail(contest, "schedule/fragment/eventDetail", "", eventId, model, redirectAttributes);
    }

    protected String _eventDetail(Contest contest, String view, String redirectView, String eventId, Model model,
                                  RedirectAttributes redirectAttributes) {
        Event event = scheduleService.getEventByIdOrCode(eventId, contest);
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

    @RequestMapping(value = "{contestCode}/schedule/ajax/upcoming-events", method = RequestMethod.GET)
    public String upcomingEvents(@PathVariable String contestCode, Model model) {
        Contest contest = getContest(contestCode, model);

        List<Event> upcomingEvents = scheduleService.getUpcomingEvents(8, contest);

        model.addAttribute("upcomingEvents", upcomingEvents);
        return "timeline/fragment/timelineUpcomingEvents";
    }

    @RequestMapping(value = "/{contestCode}/venues", method = RequestMethod.GET)
    public String venues(@PathVariable String contestCode, Model model, SitePreference sitePreference) {
        Contest contest = getContest(contestCode, model);

        model.addAttribute("venues", locationRepository.findByContestOrderByNameAsc(contest));
        model.addAttribute("pageTitle", getMessage("nav.venues"));
        model.addAttribute("sideMenuActive", "schedule");

        return resolveView("schedule/venues", "schedule/venues_mobile", sitePreference);
    }

    @RequestMapping(value = "/{contestCode}/venue/{venueId}", method = RequestMethod.GET)
    public String venueDetail(@PathVariable String contestCode, @PathVariable String venueId, Model model, HttpServletRequest request,
                              HttpServletResponse response) {
        Contest contest = getContest(contestCode, model);

        SitePreference sitePreference = SitePreferenceUtils.getCurrentSitePreference(request);
        if (!sitePreference.isMobile()) {
            return "redirect:" + getContestURL(contestCode) + "/venues";
        }


        _venueDetail(venueId, contest, model);
        model.addAttribute("sideMenuActive", "schedule");

        return "schedule/venueDetail";
    }

    @RequestMapping(value = "/{contestCode}/venue/ajax/{venueId}", method = RequestMethod.GET)
    public String venueDetailAjax(@PathVariable String contestCode, @PathVariable String venueId, Model model, HttpServletRequest request,
                                  HttpServletResponse response) {
        Contest contest = getContest(contestCode, model);

        _venueDetail(venueId, contest, model);
        model.addAttribute("showHeadline", true);

        return "schedule/fragment/venueDetail";
    }

    protected void _venueDetail(String venueId, Contest contest, Model model) {
        Location location;
        try {
            Long id = Long.parseLong(venueId);
            location = locationRepository.findOne(id);
        } catch (Exception ex) {
            location = locationRepository.findByCodeAndContest(venueId, contest);
        }

        model.addAttribute("venue", location);
        model.addAttribute("schedule", scheduleService.getScheduleEventsInLocation(location));
    }

    @RequestMapping(value = "/{contestCode}/schedule/scheduleRoles", method = RequestMethod.GET)
    public String scheduleRoles(@PathVariable String contestCode, Model model, @CookieValue(value = "scheduleRoles", required = false) String scheduleRoles) {
        Contest contest = getContest(contestCode, model);

        List<EventRole> roles = eventRoleRepository.findByContestOrderByNameAsc(contest);


        model.addAttribute("roles", roles);
        model.addAttribute("activeRoles", scheduleService.getActiveEventRoleMapping(scheduleRoles));

        return "schedule/fragment/scheduleRolesSettings";
    }

    @RequestMapping(value = "/{contestCode}/schedule/updateScheduleRole", method = RequestMethod.POST)
    public String updateScheduleRole(@PathVariable String contestCode, @RequestParam(value = "scheduleRoles[]", required = false) String param,
                                     HttpServletRequest request, HttpServletResponse response) {
        Contest contest = getContest(contestCode, null);

        if (param == null) {
            param = scheduleService.getAllScheduleRoles(contest);
        }
        CookieUtils.setCookie(request, response, "scheduleRoles", param);

        return "redirect:" + getContestURL(contestCode) + "/my-schedule";
    }
}
