package com.myicpc.controller.social.gallery;

import com.myicpc.controller.GeneralController;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.social.Notification;
import com.myicpc.social.GalleryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Roman Smetana
 */
@Controller
public class GalleryController extends GeneralController {

    @Autowired
    private GalleryService galleryService;

    @RequestMapping(value = "/{contestCode}/gallery", method = RequestMethod.GET)
    public String gallery(@PathVariable String contestCode, Model model) {
        Contest contest = getContest(contestCode, model);
        List<Notification> notifications = galleryService.getGalleryNotifications(contest);

        initGalleryModel(model, notifications, "gallery");

        return "gallery/gallery";
    }

    @RequestMapping(value = "/{contestCode}/photos", method = RequestMethod.GET)
    public String photos(@PathVariable String contestCode, Model model) {
        Contest contest = getContest(contestCode, model);
        List<Notification> notifications = galleryService.getPhotosNotifications(contest);

        initGalleryModel(model, notifications, "photos");

        return "gallery/gallery";
    }

    @RequestMapping(value = "/{contestCode}/videos", method = RequestMethod.GET)
    public String videos(@PathVariable String contestCode, Model model) {
        Contest contest = getContest(contestCode, model);
        List<Notification> notifications = galleryService.getVideosNotifications(contest);

        initGalleryModel(model, notifications, "videos");

        return "gallery/gallery";
    }

    private void initGalleryModel(final Model model, final List<Notification> notifications, String media) {
        model.addAttribute("notifications", notifications);
        model.addAttribute("lastNotificationId", galleryService.getLastNotificationId(notifications));
        model.addAttribute("sideMenuActive", "gallery");
        model.addAttribute("pageTitle", getMessage("nav."+media));
        model.addAttribute("active", media);
        // TODO
//        model.addAttribute("picasaUserId", WebServiceUtils.getPicasaUsername());
//        model.addAttribute("picasaAlbumId", WebServiceUtils.getCrowdPicasaAlbumId());
//        model.addAttribute("showUploadButton", !StringUtils.isEmpty(WebServiceUtils.getPicasaUsername()));

        model.addAttribute("showUploadButton", true);
    }

    @RequestMapping(value = "/{contestCode}/gallery/loadMore", method = RequestMethod.GET)
    public String galleryLoadMore(@PathVariable String contestCode,
                                  @RequestParam(value = "since-notification-id", required = false) Long lastNotificationId,
                                  @RequestParam(value = "media", required = false) String media,
                                  Model model) {
        Contest contest = getContest(contestCode, model);

        List<Notification> notifications = new ArrayList<>();
        if (lastNotificationId != null) {
            if ("photos".equalsIgnoreCase(media)) {
                notifications = galleryService.getPhotosNotifications(contest, lastNotificationId);
            } else if ("videos".equalsIgnoreCase(media)) {
                notifications = galleryService.getVideosNotifications(contest, lastNotificationId);
            } else {
                notifications = galleryService.getGalleryNotifications(contest, lastNotificationId);
            }
        }

        model.addAttribute("notifications", notifications);
        model.addAttribute("lastNotificationId", galleryService.getLastNotificationId(notifications));

        return "gallery/fragment/galleryTiles";
    }


}
