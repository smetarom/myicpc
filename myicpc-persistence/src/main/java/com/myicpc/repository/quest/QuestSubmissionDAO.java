package com.myicpc.repository.quest;

import com.myicpc.dto.quest.QuestSubmissionFilter;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.quest.QuestSubmission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Custom DAO methods for {@link QuestSubmissionRepository}
 *
 * @author Roman Smetana
 */
public interface QuestSubmissionDAO {
    /**
     * Filter Quest submissions by {@link QuestSubmissionFilter}
     *
     * @param submissionFilter submission filter
     * @param contest          contest
     * @param pageable         page controller
     * @return filtered page of Quest notifications
     */
    Page<QuestSubmission> getFiltredQuestSumbissions(QuestSubmissionFilter submissionFilter, Contest contest, Pageable pageable);
}
