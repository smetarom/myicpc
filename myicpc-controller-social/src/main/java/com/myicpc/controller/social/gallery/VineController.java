package com.myicpc.controller.social.gallery;

import com.myicpc.controller.GeneralController;
import com.myicpc.model.contest.Contest;
import com.myicpc.service.dto.GlobalSettings;
import com.myicpc.service.exception.WebServiceException;
import com.myicpc.service.settings.GlobalSettingsService;
import com.myicpc.social.VineService;
import com.myicpc.social.dto.InstagramUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Roman Smetana
 */
@Controller
public class VineController extends GeneralController {
    private static final Logger logger = LoggerFactory.getLogger(VineController.class);

    @Autowired
    private VineService vineService;

    @Autowired
    private GlobalSettingsService globalSettingsService;

    @RequestMapping(value = "{contestCode}/vine/new", method = RequestMethod.GET)
    public String getNew(@PathVariable final String contestCode, final RedirectAttributes redirectAttributes) {
        Contest contest = getContest(contestCode, null);

        try {
            vineService.getNewPosts(contest);
        } catch (WebServiceException e) {
            e.printStackTrace();
        }
        return null;
    }
}
