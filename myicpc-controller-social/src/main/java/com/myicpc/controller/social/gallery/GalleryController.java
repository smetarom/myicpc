package com.myicpc.controller.social.gallery;

import com.myicpc.controller.GeneralController;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.social.Notification;
import com.myicpc.service.exception.WebServiceException;
import com.myicpc.social.service.GalleryService;
import com.myicpc.social.service.PicasaService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.ArrayList;
import java.util.List;

/**
 * @author Roman Smetana
 */
@Controller
public class GalleryController extends GeneralController {
    private static final Logger logger = LoggerFactory.getLogger(GalleryController.class);

    @Autowired
    private GalleryService galleryService;

    @Autowired
    private PicasaService picasaService;

    @RequestMapping(value = "/{contestCode}/gallery", method = RequestMethod.GET)
    public String gallery(@PathVariable String contestCode, Model model) {
        Contest contest = getContest(contestCode, model);
        List<Notification> notifications = galleryService.getGalleryNotifications(contest);

        initGalleryModel(model, notifications, contest, "gallery");

        return "gallery/gallery";
    }

    @RequestMapping(value = "/{contestCode}/photos", method = RequestMethod.GET)
    public String photos(@PathVariable String contestCode, Model model) {
        Contest contest = getContest(contestCode, model);
        List<Notification> notifications = galleryService.getPhotosNotifications(contest);

        initGalleryModel(model, notifications, contest, "photos");

        return "gallery/gallery";
    }

    @RequestMapping(value = "/{contestCode}/videos", method = RequestMethod.GET)
    public String videos(@PathVariable String contestCode, Model model) {
        Contest contest = getContest(contestCode, model);
        List<Notification> notifications = galleryService.getVideosNotifications(contest);

        initGalleryModel(model, notifications, contest, "videos");

        return "gallery/gallery";
    }

    private void initGalleryModel(final Model model, final List<Notification> notifications, final Contest contest, String media) {
        model.addAttribute("notifications", notifications);
        model.addAttribute("lastNotificationId", galleryService.getLastNotificationId(notifications));
        model.addAttribute("sideMenuActive", "gallery");
        model.addAttribute("pageTitle", getMessage("nav."+media));
        model.addAttribute("active", media);
        // TODO
//        model.addAttribute("picasaUserId", contest.getWebServiceSettings().getPicasaUsername());
//        model.addAttribute("picasaAlbumId", WebServiceUtils.getCrowdPicasaAlbumId());
        model.addAttribute("showUploadButton", !StringUtils.isEmpty(contest.getWebServiceSettings().getPicasaUsername()));

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

    /**
     * Upload a photo to private Picasa gallery
     *
     * @param model
     * @param caption
     *            photo caption
     * @param file
     *            uploaded photo
     * @param redirectAttributes
     * @return view
     */
    @RequestMapping(value = "/{contestCode}/gallery/uploadPhoto", method = RequestMethod.POST)
    public String uploadPhoto(@PathVariable final String contestCode, Model model, @RequestParam("caption") String caption,
                              @RequestParam("file") MultipartFile file,
                              RedirectAttributes redirectAttributes){
        Contest contest = getContest(contestCode, model);

        try {
            picasaService.uploadPrivatePicasaEntry(caption, file, contest);
            // TODO add a new email notification on a new photo
            successMessage(redirectAttributes, "crowdGallery.successMessage");
        } catch (WebServiceException | IOException e) {
            logger.error("User photo upload to Picasa failed.", e);
            errorMessage(redirectAttributes, "crowdGallery.errorMessage");
        }

        return "redirect:" + getContestURL(contestCode) + "/gallery";
    }


}
