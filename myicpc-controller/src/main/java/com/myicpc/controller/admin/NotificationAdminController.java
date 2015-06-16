package com.myicpc.controller.admin;

import com.myicpc.controller.GeneralAdminController;
import com.myicpc.enums.NotificationType;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.social.BlacklistedUser;
import com.myicpc.model.social.Notification;
import com.myicpc.repository.social.AdminNotificationRepository;
import com.myicpc.repository.social.BlacklistedUserRepository;
import com.myicpc.repository.social.NotificationRepository;
import com.myicpc.service.dto.filter.NotificationFilterDTO;
import com.myicpc.service.notification.BlacklistService;
import com.myicpc.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Roman Smetana
 */
@Controller
@SessionAttributes(value = {"notificationFilter"})
public class NotificationAdminController extends GeneralAdminController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private BlacklistService blacklistService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private BlacklistedUserRepository blacklistedUserRepository;

    /**
     * Shows all notifications administration
     *
     * @param model
     * @param pageable
     * @param session
     * @return view
     */
    @RequestMapping(value = "/private/{contestCode}/notifications", method = RequestMethod.GET)
    public String socialNotifications(@PathVariable String contestCode, Model model, @PageableDefault(value = 100, page = 0) Pageable pageable, HttpSession session) {
        Contest contest = getContest(contestCode, model);
        NotificationFilterDTO notificationFilter = (NotificationFilterDTO) session.getAttribute("notificationFilter");
        if (notificationFilter == null) {
            notificationFilter = new NotificationFilterDTO();
        }
        return filterSocialNotifications(model, pageable, notificationFilter, contest);
    }

    /**
     * Filters the list of notifications
     *
     * @param model
     * @param pageable
     * @param notificationFilter
     *            notification filter
     * @return view
     */
    @RequestMapping(value = "/private/{contestCode}/notifications", method = RequestMethod.POST)
    public String socialNotificationsPOST(@PathVariable String contestCode, Model model, @PageableDefault(value = 100, page = 0) Pageable pageable,
                                          @ModelAttribute("notificationFilter") NotificationFilterDTO notificationFilter) {
        Contest contest = getContest(contestCode, model);
        return filterSocialNotifications(model, pageable, notificationFilter, contest);
    }

    /**
     * Filters the list of notifications
     *
     * @param model
     * @param pageable
     * @param notificationFilter
     *            notification filter
     * @return view
     */
    protected String filterSocialNotifications(Model model, Pageable pageable, NotificationFilterDTO notificationFilter, Contest contest) {
        if (notificationFilter.getTitle() == null) {
            notificationFilter.setTitle("");
        }
        if (notificationFilter.getBody() == null) {
            notificationFilter.setBody("");
        }

        pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC, "id", "notificationType", "title");

        List<NotificationType> notificationTypes = new ArrayList<>();
        notificationTypes.addAll(Arrays.asList(NotificationType.values()));
        Collections.sort(notificationTypes, new Comparator<NotificationType>() {
            @Override
            public int compare(NotificationType o1, NotificationType o2) {
                return o1.getDescription().compareToIgnoreCase(o2.getDescription());
            }
        });
        Page<Notification> notifications = notificationRepository.findFilteredNotifications(
                notificationFilter.getNotificationType() == null ? notificationTypes : Arrays.asList(notificationFilter.getNotificationType()),
                notificationFilter.getTitle() + "%", "%" + notificationFilter.getBody() + "%", contest, pageable);

        model.addAttribute("notificationPage", notifications);
        model.addAttribute("notifications", notifications.getContent());
        model.addAttribute("notificationFilter", notificationFilter);
        model.addAttribute("notificationTypes", notificationTypes);

        return "private/notification/socialNotifications";
    }

    /**
     * Shows all featured notifications administration
     *
     * @param model
     * @return view
     */
    @RequestMapping(value = "/private/{contestCode}/notifications/featured", method = RequestMethod.GET)
    public String featuredNotifications(@PathVariable String contestCode, Model model) {
        Contest contest = getContest(contestCode, model);
        List<Long> ignoredFeatured = new ArrayList<>();
        ignoredFeatured.add(-1L);
        List<Notification> notifications = notificationService.getFeaturedNotifications(ignoredFeatured, contest);

        model.addAttribute("notifications", notifications);

        return "private/notification/featuredNotifications";
    }

    /**
     * Processes a removal of notification from featured notifications
     *
     * @param notificationId
     *            notification ID
     * @return view
     */
    @RequestMapping(value = "/private/{contestCode}/notifications/featured/{notificationId}/remove", method = RequestMethod.GET)
    @ResponseBody
    public void removeFromFeaturedNotifications(@PathVariable Long notificationId) {
        Notification notification = notificationRepository.findOne(notificationId);
        if (notification == null) {
            return;
        }
        notification.setFeaturedEligible(false);
        notificationRepository.save(notification);
    }

    @RequestMapping(value = "/private/{contestCode}/notifications/suspicious", method = RequestMethod.GET)
    public String suspiciousNotifications(@PathVariable String contestCode, Model model) {
        Contest contest = getContest(contestCode, model);
        List<Notification> notifications = notificationRepository.findByOffensiveAndContestAndDeletedOrderByIdDesc(true, contest, false);

        for (Notification notification : notifications) {
            blacklistService.hightlightSwearWords(notification);
        }

        model.addAttribute("notifications", notifications);

        return "private/notification/suspiciousNotifications";
    }

    /**
     * Processes an ignore of suspicious flag
     *
     * @param notificationId
     *            notification ID
     * @return view
     */
    @RequestMapping(value = "/private/{contestCode}/notifications/suspicious/{notificationId}/ignore", method = RequestMethod.GET)
    @ResponseBody
    public void ignoreSuspiciousNotifications(@PathVariable Long notificationId) {
        Notification notification = notificationRepository.findOne(notificationId);
        if (notification == null) {
            return;
        }
        notification.setOffensive(false);
        notificationRepository.save(notification);
    }

    /**
     * Processes ban of the notification author
     *
     * @param notificationId
     *            notification ID
     * @return view
     */
    @RequestMapping(value = "/private/{contestCode}/notifications/{notificationId}/ban", method = RequestMethod.GET)
    @ResponseBody
    public void banSuspiciousNotifications(@PathVariable Long notificationId) {
        Notification notification = notificationRepository.findOne(notificationId);
        if (notification == null) {
            return;
        }
        blacklistService.banAuthor(notification);
    }

    /**
     * Processes a delete of notification
     *
     * @param notificationId
     *            notification ID
     * @param model
     * @return view
     */
    @RequestMapping(value = "/private/{contestCode}/notifications/{notificationId}/delete", method = RequestMethod.GET)
    @ResponseBody
    public void deleteNotifications(@PathVariable Long notificationId, Model model) {
        Notification notification = notificationRepository.findOne(notificationId);
        if (notification == null) {
            return;
        }
        blacklistService.markPostDeleted(notification);
    }

    /**
     * Shows a blacklist administration
     *
     * @param model
     * @return view
     */
    @RequestMapping(value = "/private/{contestCode}/blacklist", method = RequestMethod.GET)
    public String blacklist(@PathVariable String contestCode, Model model) {
        Contest contest = getContest(contestCode, model);
        List<BlacklistedUser> blacklists = blacklistedUserRepository.findByContestOrderByUsernameAscBlacklistedUserTypeAsc(contest);

        model.addAttribute("blacklists", blacklists);
        model.addAttribute("blacklistTypes", BlacklistedUser.BlacklistedUserType.values());
        return "private/notification/blacklist";
    }

    @RequestMapping(value = "/private/{contestCode}/blacklist", method = RequestMethod.POST)
    public String addToTwitterBlacklist(@PathVariable String contestCode, @RequestParam String username, @RequestParam String blacklistType, Model model) {
        Contest contest = getContest(contestCode, model);
        BlacklistedUser.BlacklistedUserType type = BlacklistedUser.BlacklistedUserType.valueOf(blacklistType);
        blacklistService.addUsernameToBlacklist(username, type, contest);
        return "redirect:/private" + getContestURL(contestCode) + "/blacklist";
    }

    /**
     * Shows a preview of notification
     *
     * @param notificationId
     * @param model
     * @return view
     */
    @RequestMapping(value = "/private/{contestCode}/notifications/{notificationId}/preview", method = RequestMethod.GET)
    public String previewNotifications(@PathVariable Long notificationId, Model model) {
        Notification notification = notificationRepository.findOne(notificationId);
        if (notification == null) {
            return null;
        }
        model.addAttribute("notification", notification);
        return "private/notification/fragment/notificationPreview";
    }

    @RequestMapping(value = "/private/{contestCode}/blacklist/removeUserFromBlacklist/{blacklistId}", method = RequestMethod.GET)
    public String removeUserFromBlacklist(@PathVariable String contestCode, @PathVariable Long blacklistId) {
        blacklistedUserRepository.delete(blacklistId);
        return "redirect:/private" + getContestURL(contestCode) + "/blacklist";
    }
}