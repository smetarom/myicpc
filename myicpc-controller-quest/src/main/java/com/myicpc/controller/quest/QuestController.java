package com.myicpc.controller.quest;

import com.myicpc.controller.GeneralController;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.quest.QuestChallenge;
import com.myicpc.model.quest.QuestLeaderboard;
import com.myicpc.model.quest.QuestParticipant;
import com.myicpc.model.quest.QuestSubmission;
import com.myicpc.model.quest.QuestSubmission.QuestSubmissionState;
import com.myicpc.model.social.Notification;
import com.myicpc.repository.quest.QuestChallengeRepository;
import com.myicpc.repository.quest.QuestLeaderboardRepository;
import com.myicpc.repository.quest.QuestParticipantRepository;
import com.myicpc.repository.quest.QuestSubmissionRepository;
import com.myicpc.service.exception.ReportException;
import com.myicpc.service.quest.QuestService;
import com.myicpc.service.quest.report.QuestReportService;
import net.sf.dynamicreports.report.exception.DRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.site.SitePreference;
import org.springframework.mobile.device.site.SitePreferenceUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author Roman Smetana
 */
@Controller
public class QuestController extends GeneralController {

    @Autowired
    private QuestService questService;

    @Autowired
    private QuestSubmissionRepository submissionRepository;

    @Autowired
    private QuestChallengeRepository challengeRepository;

    @Autowired
    private QuestParticipantRepository participantRepository;

    @Autowired
    private QuestReportService questReportService;

    @Autowired
    private QuestLeaderboardRepository leaderboardRepository;

    @RequestMapping(value = "/{contestCode}/quest", method = RequestMethod.GET)
    public String questHomepage(@PathVariable final String contestCode, Model model) {
        Contest contest = getContest(contestCode, model);

        List<QuestSubmission> list = submissionRepository.getVotesInProgressSubmissions(contest);
        if (list.size() >= 4) {
            model.addAttribute("voteCandidates", list);
        }

        List<QuestChallenge> challenges = challengeRepository.findByContestAvailableChallenges(new Date(), contest);
        model.addAttribute("challenges", challenges);

        List<Notification> questNotifications = questService.getQuestNotifications(contest);
        model.addAttribute("notifications", questNotifications);
        model.addAttribute("availableNotificationTypes", QuestService.QUEST_TIMELINE_TYPES);
        model.addAttribute("questHashtag", "#" + contest.getQuestConfiguration().getHashtagPrefix());

        return "quest/quest";
    }

    @RequestMapping(value = "/{contestCode}/quest/challenges", method = RequestMethod.GET)
    public String questChallenges(@PathVariable final String contestCode, Model model, SitePreference sitePreference) {
        Contest contest = getContest(contestCode, model);

        List<QuestChallenge> challenges = challengeRepository.findOpenChallengesByContestOrderByName(new Date(), contest);
        QuestService.applyHashtagPrefix(contest.getQuestConfiguration().getHashtagPrefix(), challenges);
        model.addAttribute("challenges", challenges);

        return resolveView("quest/challenges", "quest/challenges_mobile", sitePreference);
    }

    @RequestMapping(value = "/{contestCode}/quest/challenges/participant/{participantId}", method = RequestMethod.GET)
    public String questParticipantChallenges(@PathVariable final String contestCode,
                                             @PathVariable Long participantId,
                                             Model model, RedirectAttributes redirectAttributes,
                                             SitePreference sitePreference) {
        Contest contest = getContest(contestCode, model);

        QuestParticipant questParticipant = participantRepository.findOne(participantId);
        if (questParticipant == null) {
            errorMessage(redirectAttributes, "quest.challenges.missing.notFound");
            return "redirect:" + getContestURL(contestCode) + "/quest/challenges";
        }
        List<QuestChallenge> challenges = challengeRepository.findOpenChallengesByQuestParticipantOrderByName(new Date(), questParticipant, contest);
        QuestService.applyHashtagPrefix(contest.getQuestConfiguration().getHashtagPrefix(), challenges);
        model.addAttribute("challenges", challenges);

        return resolveView("quest/challenges", "quest/challenges_mobile", sitePreference);
    }

    @RequestMapping(value = "/{contestCode}/quest/challenge/{challengeId}", method = RequestMethod.GET)
    public String questChallengeDetail(@PathVariable final String contestCode, @PathVariable String challengeId, Model model, RedirectAttributes redirectAttributes, final Device device) {
        try {
            QuestChallenge challenge = initiateChallengeDetailModel(challengeId, contestCode, model, device);
            // TODO [rs] could it be more optimized than return the whole list
            List<QuestChallenge> challenges = challengeRepository.findOpenChallengesByContestOrderByName(new Date(), challenge.getContest());
            int index = challenges.indexOf(challenge);
            if (index != -1 && challenges.size() > 1) {
                int prevIndex = index > 0 ? index - 1 : challenges.size() - 1;
                int nextIndex = index < challenges.size() - 1 ? index + 1 : 0;
                model.addAttribute("prevChallenge", challenges.get(prevIndex));
                model.addAttribute("nextChallenge", challenges.get(nextIndex));
            } else {
                model.addAttribute("disablePagination", true);
            }
        } catch (EntityNotFoundException e) {
            errorMessage(redirectAttributes, "quest.challenge.notFound");
            return "redirect:" + getContestURL(contestCode) + "/quest/challenges";
        }
        model.addAttribute("hideTitle", true);
        return "quest/challengeDetail";
    }

    @RequestMapping(value = "/{contestCode}/quest/ajax/challenge/{challengeId}", method = RequestMethod.GET)
    public String questChallengeAjaxDetail(@PathVariable final String contestCode, @PathVariable String challengeId, Model model, RedirectAttributes redirectAttributes, final Device device) {
        try {
            initiateChallengeDetailModel(challengeId, contestCode, model, device);
        } catch (EntityNotFoundException e) {
            errorMessage(redirectAttributes, "quest.challenge.notFound");
            return "redirect:" + getContestURL(contestCode) + "/quest/challenges";
        }
        return "quest/fragment/challengeDetail";
    }

    @RequestMapping(value = "/{contestCode}/quest/challenge/missing-challenges", method = RequestMethod.POST)
    public String questChallengeAjaxDetail(@PathVariable final String contestCode,
                                           @RequestParam(required = false) String twitterUsername,
                                           @RequestParam(required = false) String vineUsername,
                                           @RequestParam(required = false) String instagramUsername,
                                           RedirectAttributes redirectAttributes) {
        Contest contest = getContest(contestCode, null);
        QuestParticipant questParticipant = questService.getQuestParticipantBySocialUsernames(twitterUsername, vineUsername, instagramUsername, contest);
        if (questParticipant == null) {
            errorMessage(redirectAttributes, "quest.challenges.missing.notFound");
            return "redirect:" + getContestURL(contestCode) + "/quest/challenges";
        }
        return "redirect:" + getContestURL(contestCode) + "/quest/challenges/participant/" + questParticipant.getId();
    }

    @RequestMapping(value = "/{contestCode}/quest/challenge/QuestGuide.pdf", method = RequestMethod.GET)
    public void questGuideExport(@PathVariable final String contestCode, HttpServletResponse response) {
        Contest contest = getContest(contestCode, null);
        try {
            List<QuestChallenge> challenges = challengeRepository.findCurrentChallengesByContestOrderByName(new Date(), contest);
            QuestService.applyHashtagPrefix(contest.getQuestConfiguration().getHashtagPrefix(), challenges);
            questReportService.downloadQuestChallengesGuide(challenges, response.getOutputStream(), false);
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment; filename=\"document.pdf\"");
            response.flushBuffer();
        } catch (DRException | IOException ex) {
            throw new ReportException(ex);
        }
    }

    @RequestMapping(value = "/{contestCode}/quest/leaderboard", method = RequestMethod.GET)
    public String leaderboard(@PathVariable final String contestCode, Model model, HttpServletRequest request, SitePreference sitePreference) {
        Contest contest = getContest(contestCode, model);

        List<QuestLeaderboard> leaderboards = leaderboardRepository.findByContestAndPublishedOrderByNameAsc(contest, true);

        model.addAttribute("leaderboards", leaderboards);
        if (!leaderboards.isEmpty()) {
            QuestLeaderboard activeLeaderboard = leaderboards.get(0);
            model.addAttribute("activeLeaderboard", activeLeaderboard);
            initiateLeaderboard(activeLeaderboard, contest, model, request);
        }

        return resolveView("quest/leaderboard", "quest/leaderboard_mobile", sitePreference);
    }

    @RequestMapping(value = "/{contestCode}/quest/leaderboard/{urlCode}", method = RequestMethod.GET)
    public String leaderboard(@PathVariable String urlCode, @PathVariable final String contestCode, Model model, HttpServletRequest request, SitePreference sitePreference) {
        Contest contest = getContest(contestCode, model);

        List<QuestLeaderboard> leaderboards = leaderboardRepository.findByContestAndPublishedOrderByNameAsc(contest, true);
        QuestLeaderboard activeLeaderboard = leaderboardRepository.findByUrlCodeAndContestAndPublished(urlCode, contest, true);

        model.addAttribute("leaderboards", leaderboards);
        model.addAttribute("activeLeaderboard", activeLeaderboard);
        initiateLeaderboard(activeLeaderboard, contest, model, request);

        return resolveView("quest/leaderboard", "quest/leaderboard_mobile", sitePreference);
    }

    @RequestMapping(value = "/{contestCode}/quest/leaderboard/{urlCode}/update", method = RequestMethod.GET)
    public String leaderboardUpdate(@PathVariable String urlCode, @PathVariable final String contestCode, Model model, HttpServletRequest request, SitePreference sitePreference) {
        Contest contest = getContest(contestCode, model);

        QuestLeaderboard activeLeaderboard = leaderboardRepository.findByUrlCodeAndContestAndPublished(urlCode, contest, true);

        model.addAttribute("activeLeaderboard", activeLeaderboard);
        model.addAttribute("includeJavascript", true);
        initiateLeaderboard(activeLeaderboard, contest, model, request);

        return resolveView("quest/fragment/leaderboard_desktop", "quest/fragment/leaderboard_mobile", sitePreference);
    }

    private void initiateLeaderboard(QuestLeaderboard activeLeaderboard, Contest contest, Model model, HttpServletRequest request) {
        SitePreference sitePreference = SitePreferenceUtils.getCurrentSitePreference(request);

        List<QuestParticipant> participants = questService.getParticipantsWithRoles(activeLeaderboard.getContestParticipantRoles(), contest, !sitePreference.isMobile());
        List<QuestChallenge> challenges = challengeRepository.findOpenChallengesByContestOrderByHashtag(new Date(), contest);
        QuestService.applyHashtagPrefix(contest.getQuestConfiguration().getHashtagPrefix(), challenges);

        model.addAttribute("activeTab", activeLeaderboard.getUrlCode());

        model.addAttribute("participants", participants);
        model.addAttribute("challenges", challenges);

    }


    private QuestChallenge initiateChallengeDetailModel(final String challengeId, final String contestCode, final Model model, Device device) {
        Contest contest = getContest(contestCode, model);

        QuestChallenge challenge;
        try {
            Long id = Long.parseLong(challengeId);
            challenge = challengeRepository.findOne(id);
        } catch (Exception ex) {
            String code = challengeId.substring(contest.getQuestConfiguration().getHashtagPrefix().length());
            challenge = challengeRepository.findByHashtagSuffix(code);
        }
        if (challenge == null) {
            throw new EntityNotFoundException();
        }
        challenge.setHashtagPrefix(contest.getQuestConfiguration().getHashtagPrefix());
        model.addAttribute("device", device);
        model.addAttribute("challenge", challenge);
        model.addAttribute("acceptedSubmissions", submissionRepository.findByChallengeAndSubmissionStateOrderByNotificationTimestampDesc(challenge, QuestSubmissionState.ACCEPTED));
        model.addAttribute("pendingSubmissions", submissionRepository.findByChallengeAndSubmissionStateOrderByNotificationTimestampDesc(challenge, QuestSubmissionState.PENDING));
        model.addAttribute("rejectedSubmissions", submissionRepository.findByChallengeAndSubmissionStateOrderByNotificationTimestampDesc(challenge, QuestSubmissionState.REJECTED));

        return challenge;
    }

}
