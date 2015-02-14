package com.myicpc.controller.schedule.admin;

import com.myicpc.controller.GeneralAdminController;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.schedule.Event;
import com.myicpc.model.schedule.EventRole;
import com.myicpc.model.schedule.Location;
import com.myicpc.model.schedule.ScheduleDay;
import com.myicpc.repository.schedule.EventRepository;
import com.myicpc.repository.schedule.EventRoleRepository;
import com.myicpc.repository.schedule.LocationRepository;
import com.myicpc.repository.schedule.ScheduleDayRepository;
import com.myicpc.service.exception.BusinessValidationException;
import com.myicpc.service.schedule.ScheduleMngmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Roman Smetana
 */
@Controller
@SessionAttributes({ "event", "location", "scheduleDay", "eventRole" })
public class ScheduleAdminController extends GeneralAdminController {

    @Autowired
    private ScheduleMngmService scheduleMngmService;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private ScheduleDayRepository scheduleDayRepository;

    @Autowired
    private EventRoleRepository eventRoleRepository;

    @RequestMapping(value = "/private/{contestCode}/schedule", method = RequestMethod.GET)
    public String list(@PathVariable final String contestCode, final Model model) {
        Contest contest = getContest(contestCode, model);

        model.addAttribute("events", eventRepository.findByContestOrderByStartDateAsc(contest));
        model.addAttribute("locations", locationRepository.findByContestOrderByNameAsc(contest));
        model.addAttribute("scheduleDays", scheduleDayRepository.findByContestOrderByDateAsc(contest));
        model.addAttribute("eventRoles", eventRoleRepository.findByContestOrderByNameAsc(contest));
        return "private/schedule/schedule";
    }

    @RequestMapping(value = "/private/{contestCode}/schedule/event/create", method = RequestMethod.GET)
    public String addEvent(@PathVariable final String contestCode, final Model model) {
        Contest contest = getContest(contestCode, model);

        Event event = new Event();
        event.setContest(contest);
        model.addAttribute("event", event);
        model.addAttribute("scheduleDays", scheduleDayRepository.findByContestOrderByDateAsc(contest));
        model.addAttribute("locations", locationRepository.findByContestOrderByNameAsc(contest));
        model.addAttribute("eventRoles", eventRoleRepository.findByContestOrderByNameAsc(contest));
        model.addAttribute("headlineTitle", getMessage("scheduleAdmin.create"));
        return "private/schedule/editEvent";
    }

    @RequestMapping(value = "/private/{contestCode}/schedule/event/{eventId}/edit", method = RequestMethod.GET)
    public String editEvent(@PathVariable final String contestCode, @PathVariable final Long eventId, final Model model,
                            final RedirectAttributes redirectAttributes) {
        getContest(contestCode, model);

        Event event = eventRepository.findOne(eventId);

        if (event == null) {
            errorMessage(redirectAttributes, "scheduleAdmin.noResult");
            return "redirect:/private/" + contestCode + "/schedule";
        }

        Iterable<EventRole> roles = eventRoleRepository.findAllOrderedByNameAsc();
        Map<Long, Boolean> rolesMap = new HashMap<>();
        for (EventRole role : event.getRoles()) {
            rolesMap.put(role.getId(), true);
        }

        model.addAttribute("event", event);
        model.addAttribute("scheduleDays", scheduleDayRepository.findByContestOrderByDateAsc(event.getContest()));
        model.addAttribute("locations", locationRepository.findByContestOrderByNameAsc(event.getContest()));
        model.addAttribute("eventRoles", roles);
        model.addAttribute("rolesMap", rolesMap);
        model.addAttribute("headlineTitle", getMessage("scheduleAdmin.edit"));
        return "private/schedule/editEvent";
    }

    @RequestMapping(value = "/private/{contestCode}/schedule/event/{eventId}/delete", method = RequestMethod.GET)
    public String deleteEvent(@PathVariable final String contestCode, @PathVariable final Long eventId, final Model model,
                              final RedirectAttributes redirectAttributes) {
        Event event = eventRepository.findOne(eventId);

        if (event == null) {
            errorMessage(redirectAttributes, "scheduleAdmin.event.noResult");
            return "redirect:/private/" + contestCode + "/schedule";
        }

        scheduleMngmService.deleteEvent(event);
        successMessage(redirectAttributes, "scheduleAdmin.deleteEvent.success", event.getName());
        return "redirect:/private/" + contestCode + "/schedule";
    }

    @RequestMapping(value = "/private/{contestCode}/schedule/updateEvent", method = RequestMethod.POST)
    public String updateEvent(@PathVariable final String contestCode, @Valid @ModelAttribute("event") final Event event, final BindingResult result,
                              final Model model) {
        model.addAttribute("scheduleDays", scheduleDayRepository.findByContestOrderByDateAsc(event.getContest()));
        model.addAttribute("locations", locationRepository.findByContestOrderByNameAsc(event.getContest()));
        model.addAttribute("headlineTitle", getMessage("scheduleAdmin.edit"));
        if (result.hasErrors()) {
            return "private/schedule/editEvent";
        }

        scheduleMngmService.saveEvent(event);

        return "redirect:/private/" + contestCode + "/schedule";
    }

    @RequestMapping(value = "/private/{contestCode}/schedule/location/create", method = RequestMethod.GET)
    public String addLocation(@PathVariable final String contestCode, final Model model) {
        Contest contest = getContest(contestCode, model);

        Location location = new Location();
        location.setContest(contest);
        model.addAttribute("location", location);
        model.addAttribute("headlineTitle", getMessage("scheduleAdmin.createLocation"));
        return "private/schedule/editLocation";
    }

    @RequestMapping(value = "/private/{contestCode}/schedule/location/{locationId}/edit", method = RequestMethod.GET)
    public String editLocation(@PathVariable final String contestCode, @PathVariable final Long locationId, final Model model,
                               final RedirectAttributes redirectAttributes) {
        getContest(contestCode, model);

        Location location = locationRepository.findOne(locationId);

        if (location == null) {
            errorMessage(redirectAttributes, "scheduleAdmin.location.noResult");
            return "redirect:/private" + getContestURL(contestCode) + "/schedule";
        }

        model.addAttribute("location", location);
        model.addAttribute("headlineTitle", getMessage("scheduleAdmin.editLocation"));
        return "private/schedule/editLocation";
    }

    @RequestMapping(value = "/private/{contestCode}/schedule/location/{locationId}/delete", method = RequestMethod.GET)
    public String deleteLocation(@PathVariable final String contestCode, @PathVariable final Long locationId, final Model model,
                                 final RedirectAttributes redirectAttributes) {
        Location location = locationRepository.findOne(locationId);

        if (location == null) {
            errorMessage(redirectAttributes, "scheduleAdmin.event.noResult");
            return "redirect:/private" + getContestURL(contestCode) + "/schedule";
        }
        try {
            scheduleMngmService.deleteLocation(location);
            successMessage(redirectAttributes, "scheduleAdmin.deleteLocation.success", location.getName());
        } catch (BusinessValidationException ex) {
            errorMessage(redirectAttributes, ex.getMessageCode(), ex.getParams());
        }
        return "redirect:/private/" + contestCode + "/schedule";
    }

    @RequestMapping(value = "/private/{contestCode}/schedule/updateLocation", method = RequestMethod.POST)
    public String updateLocation(@Valid @ModelAttribute("location") Location location, @PathVariable final String contestCode, BindingResult result,
                                 Model model) {
        model.addAttribute("headlineTitle", getMessage("scheduleAdmin.editLocation"));
        if (result.hasErrors()) {
            getContest(contestCode, model);
            return "private/schedule/editLocation";
        }

        try {
            scheduleMngmService.saveLocation(location);
        } catch (BusinessValidationException ex) {
            getContest(contestCode, model);
            result.rejectValue("code", ex.getMessageCode());
            return "private/schedule/editLocation";
        }

        return "redirect:/private" + getContestURL(contestCode) + "/schedule";
    }

    @RequestMapping(value = "/private/{contestCode}/schedule/schedule-day/create", method = RequestMethod.GET)
    public String addScheduleDay(@PathVariable final String contestCode, final Model model) {
        Contest contest = getContest(contestCode, model);

        ScheduleDay scheduleDay = new ScheduleDay();
        scheduleDay.setContest(contest);
        scheduleDay.setDayOrder((int) scheduleDayRepository.countByContest(contest) + 1);

        model.addAttribute("scheduleDay", scheduleDay);
        model.addAttribute("headlineTitle", getMessage("scheduleAdmin.createScheduleDay"));
        return "private/schedule/editScheduleDay";
    }

    @RequestMapping(value = "/private/{contestCode}/schedule/schedule-day/{scheduleDayId}/edit", method = RequestMethod.GET)
    public String editScheduleDay(@PathVariable final String contestCode, @PathVariable final Long scheduleDayId, final Model model,
                                  final RedirectAttributes redirectAttributes) {
        getContest(contestCode, model);

        ScheduleDay scheduleDay = scheduleDayRepository.findOne(scheduleDayId);
        if (scheduleDay == null) {
            errorMessage(redirectAttributes, "scheduleAdmin.scheduleDay.noResult");
            return "redirect:/private/" + contestCode + "/schedule";
        }

        model.addAttribute("scheduleDay", scheduleDay);
        model.addAttribute("headlineTitle", getMessage("scheduleAdmin.editScheduleDay"));
        return "private/schedule/editScheduleDay";
    }

    @RequestMapping(value = "/private/{contestCode}/schedule/schedule-day/{scheduleDayId}/delete", method = RequestMethod.GET)
    public String deleteScheduleDay(@PathVariable final String contestCode, @PathVariable final Long scheduleDayId, final Model model,
                                    final RedirectAttributes redirectAttributes) {
        ScheduleDay scheduleDay = scheduleDayRepository.findOne(scheduleDayId);

        if (scheduleDay == null) {
            errorMessage(redirectAttributes, "scheduleAdmin.scheduleDay.noResult");
            return "redirect:/private/" + contestCode + "/schedule";
        }

        try {
            scheduleMngmService.deleteScheduleDay(scheduleDay);
            successMessage(redirectAttributes, "scheduleAdmin.deleteScheduleDay.success", scheduleDay.getName());
        } catch (BusinessValidationException ex) {
            errorMessage(redirectAttributes, ex.getMessageCode(), ex.getParams());
        }
        return "redirect:/private/" + contestCode + "/schedule";
    }

    @RequestMapping(value = "/private/{contestCode}/schedule/updateScheduleDay", method = RequestMethod.POST)
    public String updateScheduleDay(@Valid @ModelAttribute("scheduleDay") ScheduleDay scheduleDay, @PathVariable final String contestCode,
                                    BindingResult result, Model model) {
        model.addAttribute("headlineTitle", getMessage("scheduleAdmin.editScheduleDay"));
        if (result.hasErrors()) {
            getContest(contestCode, model);
            return "private/schedule/editScheduleDay";
        }

        try {
            scheduleMngmService.saveScheduleDay(scheduleDay);
        } catch (BusinessValidationException ex) {
            getContest(contestCode, model);
            result.rejectValue("dayOrder", ex.getMessageCode());
            return "private/schedule/editScheduleDay";
        }

        return "redirect:/private/" + contestCode + "/schedule";
    }

    @RequestMapping(value = "/private/{contestCode}/schedule/event-role/create", method = RequestMethod.GET)
    public String addEventRole(@PathVariable final String contestCode, final Model model) {
        Contest contest = getContest(contestCode, model);

        EventRole eventRole = new EventRole();
        eventRole.setContest(contest);
        model.addAttribute("eventRole", eventRole);
        model.addAttribute("headlineTitle", getMessage("scheduleAdmin.createEventRole"));
        return "private/schedule/editEventRole";
    }

    @RequestMapping(value = "/private/{contestCode}/schedule/event-role/{eventRoleId}/edit", method = RequestMethod.GET)
    public String editEventRole(@PathVariable final String contestCode, @PathVariable final Long eventRoleId, final Model model,
                                final RedirectAttributes redirectAttributes) {
        getContest(contestCode, model);

        EventRole role = eventRoleRepository.findOne(eventRoleId);

        if (role == null) {
            errorMessage(redirectAttributes, "scheduleAdmin.eventRole.noResult");
            return "redirect:/private/" + contestCode + "/schedule";
        }

        model.addAttribute("eventRole", role);
        model.addAttribute("headlineTitle", getMessage("scheduleAdmin.editEventRole"));
        return "private/schedule/editEventRole";
    }

    @RequestMapping(value = "/private/{contestCode}/schedule/event-role/{eventRoleId}/delete", method = RequestMethod.GET)
    public String deleteEventRole(@PathVariable final String contestCode, @PathVariable final Long eventRoleId, final Model model,
                                  final RedirectAttributes redirectAttributes) {
        EventRole role = eventRoleRepository.findOne(eventRoleId);

        if (role == null) {
            errorMessage(redirectAttributes, "scheduleAdmin.eventRole.noResult");
            return "redirect:/private/" + contestCode + "/schedule";
        }
        try {
            scheduleMngmService.deleteEventRole(role);
            successMessage(redirectAttributes, "scheduleAdmin.deleteEventRole.success", role.getName());
        } catch (BusinessValidationException ex) {
            errorMessage(redirectAttributes, ex.getMessageCode(), ex.getParams());
        }
        return "redirect:/private/" + contestCode + "/schedule";
    }

    @RequestMapping(value = "/private/{contestCode}/schedule/updateEventRole", method = RequestMethod.POST)
    public String updateEventRole(@Valid @ModelAttribute("eventRole") EventRole eventRole, @PathVariable final String contestCode,
                                  BindingResult result, Model model) {
        model.addAttribute("headlineTitle", getMessage("scheduleAdmin.editEventRole"));
        if (result.hasErrors()) {
            getContest(contestCode, model);
            return "private/schedule/editEventRole";
        }

        try {
            scheduleMngmService.saveEventRole(eventRole);
        } catch (BusinessValidationException ex) {
            getContest(contestCode, model);
            result.rejectValue("name", ex.getMessageCode());
            return "private/schedule/editEventRole";
        }

        return "redirect:/private/" + contestCode + "/schedule";
    }

    @RequestMapping(value = "/private/{contestCode}/schedule/import", method = RequestMethod.POST)
    public String importSchedule(@PathVariable final String contestCode, @RequestParam final MultipartFile rolesCSV,
                                 @RequestParam final MultipartFile daysCSV, @RequestParam final MultipartFile locationsCSV, @RequestParam final MultipartFile eventsCSV,
                                 final Model model, final RedirectAttributes redirectAttributes) throws IOException, ParseException {
        Contest contest = getContest(contestCode, model);
        try {
            scheduleMngmService.importSchedule(rolesCSV, daysCSV, locationsCSV, eventsCSV, contest);

            successMessage(redirectAttributes, "scheduleAdmin.import.success");
        } catch (ValidationException ex) {
            errorMessage(redirectAttributes, ex);
        } catch (Exception ex) {
            errorMessage(redirectAttributes, "scheduleAdmin.import.failed");
        }

        return "redirect:/private/" + contestCode + "/schedule";
    }

    @InitBinder("event")
    protected void initEventBinder(final WebDataBinder binder) {
        bindDateTimeFormat(binder);
    }

    @InitBinder("scheduleDay")
    protected void initScheduleDayBinder(final WebDataBinder binder) {
        bindDateFormat(binder);
    }

    @InitBinder
    protected void initBinder(final WebDataBinder binder) {
        binder.registerCustomEditor(Set.class, "roles", new CustomCollectionEditor(Set.class) {
            @Override
            protected Object convertElement(Object element) {
                Long id = null;

                if (element instanceof String && !(element).equals("")) {
                    id = Long.parseLong((String) element);
                } else if (element instanceof Long) {
                    id = (Long) element;
                }

                return id != null ? eventRoleRepository.findOne(id) : null;
            }
        });
    }
}
