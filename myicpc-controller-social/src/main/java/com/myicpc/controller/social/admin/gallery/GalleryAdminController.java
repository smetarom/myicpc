package com.myicpc.controller.social.admin.gallery;

import com.myicpc.controller.GeneralController;
import com.myicpc.model.contest.Contest;
import com.myicpc.service.exception.WebServiceException;
import com.myicpc.social.service.PicasaService;
import com.myicpc.social.dto.PicasaPhoto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * @author Roman Smetana
 */
@Controller
public class GalleryAdminController extends GeneralController {
    private static final Logger logger = LoggerFactory.getLogger(GalleryAdminController.class);

    @Autowired
    private PicasaService picasaService;

    @RequestMapping(value = {"/private/{contestCode}/picasa", "/private/{contestCode}/gallery"}, method = RequestMethod.GET)
    public String waitingForApproval(@PathVariable String contestCode, Model model) {
        Contest contest = getContest(contestCode, model);

        List<PicasaPhoto> newPhotos = null;
        try {
            newPhotos = picasaService.getPrivatePhotos(contest);
        } catch (WebServiceException e) {
            logger.error("Error when receiving photos from Picasa", e);
            model.addAttribute("errorMsg", getMessage("galleryAdmin.picasa.photos.error"));
        }

        model.addAttribute("newPhotos", newPhotos);

        return "private/gallery/picasa";
    }

    /**
     * Processes a delation of photo from private Picasa ablum
     *
     * @param photoId
     *            Picasa photo ID
     * @param model
     * @param redirectAttributes
     * @return redirect to gallery home page
     */
    @RequestMapping(value = "/private/{contestCode}/picasa/delete/{photoId}", method = RequestMethod.GET)
    public String deletePhoto(@PathVariable String contestCode, @PathVariable String photoId, Model model, RedirectAttributes redirectAttributes) {
        Contest contest = getContest(contestCode, model);

        try {
            picasaService.deletePrivatePhoto(photoId, contest);
            successMessage(redirectAttributes, "galleryAdmin.picasa.msg.reject", photoId);
        } catch (WebServiceException e) {
            logger.error("Reject Picasa photo error", e);
            errorMessage(redirectAttributes, "galleryAdmin.picasa.msg.reject.error", photoId);
        }

        return "redirect:/private" + getContestURL(contestCode) + "/gallery";
    }

    /**
     * Processes an approval of private Picasa photo
     *
     * @param photoId
     *            Picasa photo ID
     * @param photoTitle
     *            new photo caption
     * @param model
     * @param redirectAttributes
     * @return redirect to gallery home page
     */
    @RequestMapping(value = "/private/{contestCode}/picasa/approve/{photoId}", method = RequestMethod.POST)
    public String approvePhoto(@PathVariable String contestCode, @PathVariable String photoId, @RequestParam(value = "title", required = false) String photoTitle, Model model, RedirectAttributes redirectAttributes) {
        Contest contest = getContest(contestCode, model);

        try {
            picasaService.approvePrivatePhoto(photoId, photoTitle, contest);
            photoTitle = photoTitle != null ? photoTitle : "";
            successMessage(redirectAttributes, "galleryAdmin.picasa.msg.approved", photoTitle);
        } catch (WebServiceException e) {
            logger.error("Approval Pisaca photo error", e);
            errorMessage(redirectAttributes, "galleryAdmin.picasa.msg.approved.error", photoId);
        }

        return "redirect:/private" + getContestURL(contestCode) + "/gallery";
    }

    @RequestMapping(value = "/private/picasa/create-album/crowd", method = RequestMethod.GET)
    @ResponseBody
    public String createCrowdPicasaAlbum(@RequestParam String username, @RequestParam String password) {
        try {
            return picasaService.createCrowdAlbum(null, username, password);
        } catch (WebServiceException e) {
            logger.error(e.getMessage(), e);
            return "";
        }
    }

    @RequestMapping(value = "/private/picasa/create-album/private", method = RequestMethod.GET)
    @ResponseBody
    public String createPrivatePicasaAlbum(@RequestParam String username, @RequestParam String password) {
        try {
            return picasaService.createPrivateAlbum(null, username, password);
        } catch (WebServiceException e) {
            logger.error(e.getMessage(), e);
            return "";
        }
    }
}
