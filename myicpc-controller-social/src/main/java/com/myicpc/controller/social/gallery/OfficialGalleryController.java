package com.myicpc.controller.social.gallery;

import com.myicpc.controller.GeneralController;
import com.myicpc.model.contest.Contest;
import com.myicpc.repository.social.GalleryAlbumRepository;
import com.myicpc.repository.teamInfo.ContestParticipantRepository;
import com.myicpc.service.participant.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Roman Smetana
 */
@Controller
public class OfficialGalleryController extends GeneralController {
    @Autowired
    private ContestParticipantRepository contestParticipantRepository;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private GalleryAlbumRepository galleryAlbumRepository;

    @RequestMapping(value = "/{contestCode}/gallery/official", method = RequestMethod.GET)
    public String officialGallery(@PathVariable String contestCode, Model model) {
        Contest contest = getContest(contestCode, model);

        model.addAttribute("galleries", galleryAlbumRepository.findByContestAndPublished(contest, true, new Sort(Sort.Direction.ASC, "name")));
        model.addAttribute("contestParticipants", contestParticipantRepository.findByContest(contest));
        model.addAttribute("teams", participantService.getTeamInfosSortedByName(contest));

        return "gallery/officialGallery";
    }
}
