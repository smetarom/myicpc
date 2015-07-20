package com.myicpc.controller.kiosk;

import com.myicpc.controller.GeneralController;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.kiosk.KioskContent;
import com.myicpc.service.kiosk.KioskService;
import com.myicpc.service.schedule.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * Controller for public kiosk pages
 *
 * @author Roman Smetana
 */
@Controller
public class KioskController extends GeneralController {

    @Autowired
    private KioskService kioskService;

    @Autowired
    private ScheduleService scheduleService;

    /**
     * Social notification wall
     *
     * @param model
     * @param contestCode
     * @return JSP view
     */
    @RequestMapping(value = {"/{contestCode}/kiosk", "/{contestCode}/kiosk/feed"}, method = RequestMethod.GET)
    public String kioskFeed(Model model, @PathVariable String contestCode) {
        Contest contest = getContest(contestCode, model);

        model.addAttribute("notificationsJSON", kioskService.getKioskNotificationsJSON(contest));
        model.addAttribute("availableNotificationTypes", KioskService.KIOSK_TYPES);

        return "kiosk/kioskFeed";
    }

    /**
     * Kiosk calendar view, where each day has a list of events
     *
     * @param model
     * @param contestCode
     * @return JSP view
     */
    @RequestMapping(value = "/{contestCode}/kiosk/calendar", method = RequestMethod.GET)
    public String kioskCalendar(Model model, @PathVariable String contestCode) {
        Contest contest = getContest(contestCode, model);

        model.addAttribute("scheduleJSON", scheduleService.getScheduleDaysJSON(new Date(), contest));

        return "kiosk/kioskCalendar";
    }

    /**
     * Kiosk view, which supports custom HTML content
     *
     * @param model
     * @param contestCode
     * @return JSP view
     */
    @RequestMapping(value = "/{contestCode}/kiosk/custom", method = RequestMethod.GET)
    public String kioskCustom(Model model, @PathVariable String contestCode) {
        Contest contest = getContest(contestCode, model);
        return "kiosk/kioskCustom";
    }

    /**
     * HTML content of custom kiosk view
     *
     * @param model
     * @param contestCode
     * @return HTML code
     */
    @RequestMapping(value = "/{contestCode}/kiosk/custom/content", method = RequestMethod.GET)
    @ResponseBody
    public String kioskCustomContent(Model model, @PathVariable String contestCode) {
        Contest contest = getContest(contestCode, model);

        KioskContent activeKioskContent = kioskService.getActiveKioskContent(contest);

        if (activeKioskContent == null) {
            // TODO no active content
            return "";
        }

        return activeKioskContent.getContent();
    }
}
