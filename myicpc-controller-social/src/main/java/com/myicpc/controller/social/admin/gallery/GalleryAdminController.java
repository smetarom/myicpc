package com.myicpc.controller.social.admin.gallery;

import com.google.gdata.util.ServiceException;
import com.myicpc.controller.GeneralAdminController;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.social.GalleryAlbum;
import com.myicpc.repository.social.GalleryAlbumRepository;
import com.myicpc.service.exception.BusinessValidationException;
import com.myicpc.service.exception.WebServiceException;
import com.myicpc.social.dto.PicasaPhoto;
import com.myicpc.social.service.PicasaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * @author Roman Smetana
 */
@Controller
@SessionAttributes("galleryAlbum")
public class GalleryAdminController extends GeneralAdminController {
    private static final Logger logger = LoggerFactory.getLogger(GalleryAdminController.class);

    @Autowired
    private PicasaService picasaService;

    @Autowired
    private GalleryAlbumRepository galleryAlbumRepository;

    @RequestMapping(value = "/private/{contestCode}/gallery/official", method = RequestMethod.GET)
    public String galleryAlbums(@PathVariable String contestCode, Model model) {
        Contest contest = getContest(contestCode, model);

        model.addAttribute("galleries", galleryAlbumRepository.findByContest(contest, new Sort(Sort.Direction.ASC, "name")));

        return "private/gallery/galleryAlbums";
    }

    @RequestMapping(value = "/private/{contestCode}/gallery/official/create", method = RequestMethod.GET)
    public String createGalleryAlbum(@PathVariable String contestCode, final Model model) {
        Contest contest = getContest(contestCode, model);

        GalleryAlbum galleryAlbum = new GalleryAlbum();
        galleryAlbum.setContest(contest);
        model.addAttribute("galleryAlbum", galleryAlbum);
        model.addAttribute("mode", "create");
        model.addAttribute("headlineTitle", getMessage("officialGalleryAdmin.create"));
        return "private/gallery/editGalleryAlbum";
    }

    @RequestMapping(value = "/private/{contestCode}/gallery/official/{galleryAlbumId}/edit", method = RequestMethod.GET)
    public String editGalleryAlbum(@PathVariable final String contestCode, @PathVariable final Long galleryAlbumId, final Model model,
                           RedirectAttributes redirectAttributes) {
        getContest(contestCode, model);
        GalleryAlbum galleryAlbum = galleryAlbumRepository.findOne(galleryAlbumId);

        try {
            System.out.println(picasaService.buildGalleryAlbumNotificationBody(galleryAlbum));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        if (galleryAlbum == null) {
            errorMessage(redirectAttributes, "officialGalleryAdmin.notFound");
            return "redirect:/private" + getContestURL(contestCode) + "/gallery/official";
        }

        model.addAttribute("galleryAlbum", galleryAlbum);
        model.addAttribute("mode", "edit");
        model.addAttribute("headlineTitle", getMessage("officialGalleryAdmin.edit"));
        return "private/gallery/editGalleryAlbum";
    }

    @RequestMapping(value = "/private/{contestCode}/gallery/official/{galleryAlbumId}/delete", method = RequestMethod.GET)
    public String deleteGalleryAlbum(@PathVariable final String contestCode, @PathVariable final Long galleryAlbumId, final RedirectAttributes redirectAttributes) {
        galleryAlbumRepository.delete(galleryAlbumId);
        successMessage(redirectAttributes, "delete.success");
        return "redirect:/private" + getContestURL(contestCode) + "/gallery/official";
    }

    @RequestMapping(value = "/private/{contestCode}/gallery/official/{galleryAlbumId}/publish", method = RequestMethod.GET)
    public String publishGalleryAlbum(@PathVariable final String contestCode, @PathVariable final Long galleryAlbumId, final RedirectAttributes redirectAttributes) {
        GalleryAlbum galleryAlbum = galleryAlbumRepository.findOne(galleryAlbumId);

        if (galleryAlbum == null) {
            errorMessage(redirectAttributes, "officialGalleryAdmin.notFound");
            return "redirect:/private" + getContestURL(contestCode) + "/gallery/official";
        }
        galleryAlbum.setPublished(true);
        picasaService.saveGalleryAlbum(galleryAlbum);
        successMessage(redirectAttributes, "officialGalleryAdmin.publish.success", galleryAlbum.getName());
        return "redirect:/private" + getContestURL(contestCode) + "/gallery/official";
    }

    @RequestMapping(value = "/private/{contestCode}/gallery/official/update", method = RequestMethod.POST)
    public String updateGalleryAlbum(@PathVariable String contestCode, @Valid @ModelAttribute("galleryAlbum") GalleryAlbum galleryAlbum, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        Contest contest = getContest(contestCode, model);
        model.addAttribute("headlineTitle", getMessage("pollAdmin.edit"));
        if (result.hasErrors()) {
            return "private/gallery/editGalleryAlbum";
        }
        picasaService.saveGalleryAlbum(galleryAlbum);
        successMessage(redirectAttributes, "save.success");
        return "redirect:/private" + getContestURL(contestCode) + "/gallery/official";
    }

    @RequestMapping(value = "/private/{contestCode}/gallery/official/import", method = RequestMethod.POST)
    public String importGalleryAlbum(@PathVariable final String contestCode, @RequestParam final MultipartFile galleriesCSV, final RedirectAttributes redirectAttributes) {
        Contest contest = getContest(contestCode, null);
        picasaService.importGalleryAlbums(galleriesCSV, contest);
        return "redirect:/private" + getContestURL(contestCode) + "/gallery/official";
    }

    @RequestMapping(value = {"/private/{contestCode}/picasa", "/private/{contestCode}/gallery"}, method = RequestMethod.GET)
    public String waitingForApproval(@PathVariable String contestCode, Model model) {
        Contest contest = getContest(contestCode, model);

        List<PicasaPhoto> newPhotos = null;
        try {
            newPhotos = picasaService.getPrivatePhotos(contest);
        } catch (WebServiceException e) {
            logger.error("Error when receiving photos from Picasa", e);
            model.addAttribute("errorMsg", getMessage("galleryAdmin.picasa.photos.error"));
        } catch (BusinessValidationException e) {
            model.addAttribute("errorMsg", getMessage(e.getMessageCode()));
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
