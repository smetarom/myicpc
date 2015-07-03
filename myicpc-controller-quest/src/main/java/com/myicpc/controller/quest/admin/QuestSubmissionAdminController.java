package com.myicpc.controller.quest.admin;

import com.myicpc.controller.GeneralAdminController;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.quest.QuestSubmission;
import com.myicpc.repository.quest.QuestChallengeRepository;
import com.myicpc.repository.quest.QuestParticipantRepository;
import com.myicpc.repository.quest.QuestSubmissionRepository;
import com.myicpc.service.quest.QuestMngmService;
import com.myicpc.service.quest.QuestService;
import com.myicpc.service.quest.dto.QuestSubmissionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/**
 * @author Roman Smetana
 */
@Controller
@SessionAttributes("submissionFilter")
public class QuestSubmissionAdminController extends GeneralAdminController {

    @Autowired
    private QuestMngmService questMngmService;

    @Autowired
    private QuestChallengeRepository challengeRepository;

    @Autowired
    private QuestParticipantRepository participantRepository;

    @Autowired
    private QuestSubmissionRepository submissionRepository;

    @RequestMapping(value = "/private/{contestCode}/quest/submissions", method = RequestMethod.GET)
    public String submissions(@PathVariable final String contestCode, Model model, @PageableDefault(value = 12, page = 0) Pageable pageable,
                              HttpSession session) {
        Contest contest = getContest(contestCode, model);
        QuestSubmissionFilter submissionFilter = (QuestSubmissionFilter) session.getAttribute("submissionFilter");
        if (submissionFilter == null) {
            submissionFilter = new QuestSubmissionFilter();
            submissionFilter.setSubmissionState(QuestSubmission.QuestSubmissionState.PENDING);
        }

        initSubmissionListModel(model, submissionFilter, contest, pageable);
        return "private/quest/submissions";
    }

    /**
     * Shows a page with filtered submissions
     *
     * @param model
     * @param pageable
     * @param submissionFilter
     *            submission filer
     * @return view
     */
    @RequestMapping(value = "/private/{contestCode}/quest/submissions", method = RequestMethod.POST)
    public String submissionsPOST(@PathVariable final String contestCode, Model model, @PageableDefault(value = 12, page = 0) Pageable pageable,
                                  @ModelAttribute("submissionFilter") QuestSubmissionFilter submissionFilter) {
        Contest contest = getContest(contestCode, model);

        initSubmissionListModel(model, submissionFilter, contest, pageable);
        return "private/quest/submissions";
    }

    private void initSubmissionListModel(final Model model, final QuestSubmissionFilter submissionFilter, final Contest contest, final Pageable pageable) {
        Page<QuestSubmission> submissions = questMngmService.getFiltredQuestSumbissions(submissionFilter, contest, pageable);
        QuestService.applyHashtagPrefixToSubmissions(contest.getQuestConfiguration().getHashtagPrefix(), submissions.getContent());
        model.addAttribute("submissions", submissions);
        model.addAttribute("challenges", challengeRepository.findByContestOrderByNameAsc(contest));
        model.addAttribute("participants", participantRepository.findByContestOrderByContestParticipantLastnameAsc(contest));
        model.addAttribute("submissionFilter", submissionFilter);
        model.addAttribute("submissionStates", questMngmService.getQuestSubmissionStates());
    }

    /**
     * Processes submission rejection
     *
     * @param submissionId
     *            quest submission ID
     * @param reasonToReject
     *            reason, why the submission was rejected
     * @param redirectAttributes
     * @return redirect to quest submission home page
     */
    @RequestMapping(value = "/private/{contestCode}/quest/submission/{submissionId}/reject", method = RequestMethod.POST)
    public String rejectSubmission(@PathVariable final String contestCode, @PathVariable final Long submissionId,
                                   @RequestParam(value = "reasonToReject", required = false) final String reasonToReject, final RedirectAttributes redirectAttributes) {
        QuestSubmission submission = submissionRepository.findOne(submissionId);
        if (submission == null) {
            return "redirect:/private" + getContestURL(contestCode) + "/quest/submissions";
        }

        questMngmService.rejectQuestSubmission(submission, reasonToReject);

        return "redirect:/private" + getContestURL(contestCode) + "/quest/submissions";
    }

    /**
     * Processes submission approval
     *
     * @param submissionId
     *            quest submission ID
     * @param questPoints
     *            how many points was submission rewarded
     * @return redirect to quest submission home page
     */
    @RequestMapping(value = "/private/{contestCode}/quest/submission/{submissionId}/accept", method = RequestMethod.POST)
    public String acceptSubmission(@PathVariable final String contestCode, @PathVariable final Long submissionId,
                                   @RequestParam(value = "questPoints", required = false) final Integer questPoints) {
        QuestSubmission submission = submissionRepository.findOne(submissionId);
        if (submission == null) {
            return "redirect:/private" + getContestURL(contestCode) + "/quest/submissions";
        }

        questMngmService.acceptQuestSubmission(submission, questPoints);

        return "redirect:/private" + getContestURL(contestCode) + "/quest/submissions";
    }
}
