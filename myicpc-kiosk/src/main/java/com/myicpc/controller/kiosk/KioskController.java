package com.myicpc.controller.kiosk;

import com.myicpc.controller.GeneralController;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.schedule.ScheduleDay;
import com.myicpc.service.kiosk.KioskService;
import com.myicpc.service.schedule.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Roman Smetana
 */
@Controller
public class KioskController extends GeneralController {

    @Autowired
    private KioskService kioskService;

    @Autowired
    private ScheduleService scheduleService;

    @RequestMapping(value = {"/{contestCode}/kiosk", "/{contestCode}/kiosk/feed"}, method = RequestMethod.GET)
    public String kioskFeed(Model model, @PathVariable String contestCode) {
        Contest contest = getContest(contestCode, model);

        model.addAttribute("notificationsJSON", kioskService.getKioskNotificationsJSON(contest));
        model.addAttribute("availableNotificationTypes", KioskService.KIOSK_TYPES);

        return "kiosk/kioskFeed";
    }

    @RequestMapping(value = "/{contestCode}/kiosk/calendar", method = RequestMethod.GET)
    public String kioskCalendar(Model model, @PathVariable String contestCode) {
        Contest contest = getContest(contestCode, model);

        model.addAttribute("scheduleJSON", scheduleService.getScheduleDaysJSON(new Date(), contest));

        return "kiosk/kioskCalendar";
    }
}
