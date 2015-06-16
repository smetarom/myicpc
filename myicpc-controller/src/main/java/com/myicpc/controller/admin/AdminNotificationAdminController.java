package com.myicpc.controller.admin;

import com.myicpc.controller.GeneralAdminController;
import com.myicpc.controller.functions.JSPCustomFunctions;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.social.AdminNotification;
import com.myicpc.model.social.Notification;
import com.myicpc.repository.social.AdminNotificationRepository;
import com.myicpc.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Date;
import java.util.List;

/**
 * @author Roman Smetana
 */
@Controller
@SessionAttributes(value = {"notification"})
public class AdminNotificationAdminController extends GeneralAdminController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private AdminNotificationRepository adminNotificationRepository;

    @RequestMapping(value = "/private/{contestCode}/notifications/icpc", method = RequestMethod.GET)
    public String participants(@PathVariable String contestCode, Model model) {
        Contest contest = getContest(contestCode, model);

        model.addAttribute("notifications", adminNotificationRepository.findByContestOrderByEndDateDesc(contest));
        return "private/notification/adminNotifications";
    }

    @RequestMapping(value = "/private/{contestCode}/notifications/icpc/create", method = RequestMethod.GET)
    public String addNotification(@PathVariable String contestCode, Model model) {
        Contest contest = getContest(contestCode, model);
        AdminNotification adminNotification = new AdminNotification();
        adminNotification.setContest(contest);
        model.addAttribute("notification", adminNotification);
        model.addAttribute("headlineTitle", getMessage("notificationAdmin.create"));
        model.addAttribute("mode", "create");
        return "private/notification/editNotification";
    }
    @RequestMapping(value = "/private/{contestCode}/notifications/icpc/{notificationId}/edit", method = RequestMethod.GET)
    public String editNotification(@PathVariable String contestCode, @PathVariable Long notificationId, Model model, RedirectAttributes redirectAttributes) {
        AdminNotification notification = adminNotificationRepository.findOne(notificationId);

        if (notification == null) {
            errorMessage(redirectAttributes, "adminNotification.noResult");
            return "redirect:/private/notifications/admin";
        }
        Contest contest = getContest(contestCode, model);

        model.addAttribute("notification", notification);
        model.addAttribute("headlineTitle", getMessage("notificationAdmin.edit"));
        model.addAttribute("mode", "edit");
        return "private/notification/editNotification";
    }

    @RequestMapping(value = "/private/{contestCode}/notifications/icpc/{notificationId}/delete", method = RequestMethod.GET)
    public String deleteNotification(@PathVariable String contestCode, @PathVariable Long notificationId, Model model, RedirectAttributes redirectAttributes) {
        AdminNotification notification = adminNotificationRepository.findOne(notificationId);

        if (notification == null) {
            errorMessage(redirectAttributes, "adminNotification.noResult");
            return "redirect:/private/notifications/admin";
        }

        notificationService.deleteAdminNotification(notification);

        successMessage(redirectAttributes, "adminNotification.deleted");
        return "redirect:/private" + getContestURL(contestCode) + "/notifications/icpc";
    }

    /**
     * Processes ICPC notification update
     *
     * @param notification
     *            admin notification
     * @param result
     * @param model
     * @return redirect to admin notification home page on success, otherwise
     *         stay on admin notification edit page
     */
    @RequestMapping(value = "/private/{contestCode}/notifications/icpc/update", method = RequestMethod.POST)
    public String updatePoll(@PathVariable String contestCode, @Valid @ModelAttribute("notification") AdminNotification notification, BindingResult result, Model model) {
        model.addAttribute("headlineTitle", getMessage("notificationAdmin.edit"));
        if (result.hasErrors()) {
            return "private/notification/editNotification";
        } else if (notification.getEndDate() != null && new Date().after(notification.getEndDate())) {
            model.addAttribute("errorMessage", "adminNotification.error.dates");
            return "private/notification/editNotification";
        }

        notificationService.updateAdminNotification(notification);

        return "redirect:/private" + getContestURL(contestCode) + "/notifications/icpc";
    }

    /**
     * Processes a preview of wiki text
     *
     * @param text
     *            text with wiki syntax
     * @return formatted text
     */
    @RequestMapping(value = "/preview-wiki-text", method = RequestMethod.POST)
    @ResponseBody
    public String previewWikiText(@RequestParam("text") String text) {
        return JSPCustomFunctions.parseWikiSyntax(text);
    }

    /**
     * Bind date time format
     *
     * @param binder
     */
    @InitBinder("notification")
    protected void initEventBinder(WebDataBinder binder) {
        bindDateTimeFormat(binder);
    }
}