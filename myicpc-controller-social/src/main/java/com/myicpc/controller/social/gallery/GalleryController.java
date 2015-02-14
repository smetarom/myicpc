package com.myicpc.controller.social.gallery;

import com.myicpc.controller.GeneralController;
import com.myicpc.enums.GalleryMediaType;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.social.GalleryMedia;
import com.myicpc.service.utils.lists.MediaList;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author Roman Smetana
 */
@Controller
public class GalleryController extends GeneralController {
    public static final int PER_PAGE_GALLERY = 30;

    @RequestMapping(value = "/{contestCode}/gallery", method = RequestMethod.GET)
    public String gallery(@PathVariable String contestCode, Model model) {
        Contest contest = getContest(contestCode, model);

        List<GalleryMediaType> mediaTypes = MediaList.newList().addGallery().addInstagramImage().addInstagramVideo().addVine().addYoutube();

        Pageable pageable = new PageRequest(0, PER_PAGE_GALLERY);
//        List<GalleryMedia> medias = galleryMediaRepository.findByGalleryMediaTypesAndContest(mediaTypes, contest, pageable);


//        model.addAttribute("medias", medias);
        model.addAttribute("sideMenuActive", "gallery");
        model.addAttribute("pageTitle", getMessage("nav.gallery"));
        // TODO
//        model.addAttribute("picasaUserId", WebServiceUtils.getPicasaUsername());
//        model.addAttribute("picasaAlbumId", WebServiceUtils.getCrowdPicasaAlbumId());
//        model.addAttribute("showUploadButton", !StringUtils.isEmpty(WebServiceUtils.getPicasaUsername()));

        return "gallery/gallery";
    }
}
