package com.myicpc.service.analyst;

import com.myicpc.model.eventFeed.TeamProblem;
import org.springframework.stereotype.Service;

/**
 * @author Roman Smetana
 */
@Service
public class MessageAnalystService {
    /**
     * Get auto-generated title for run
     *
     * @param teamProblem run
     * @return notification title
     */
    public String getAnalystTitle(final TeamProblem teamProblem) {
        // TODO improve i18n
        return "Auto-generated title";
    }

    /**
     * Get auto-generated message for run
     *
     * @param teamProblem run
     * @return notification message
     */
    public String getAnalystMessage(final TeamProblem teamProblem) {
        // TODO improve i18n
        return "Auto-generated message";
    }
}
