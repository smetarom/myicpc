package com.myicpc.service.scoreboard.insight;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myicpc.model.EntityObject;
import com.myicpc.model.contest.Contest;
import com.myicpc.repository.eventFeed.TeamProblemRepository;
import com.myicpc.service.settings.GlobalSettingsService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * Common insight service, which defines the common methods to all
 * insight services based on event feed
 *
 * @author Roman Smetana
 */
public abstract class AbstractInsightService<T extends EntityObject> {

    @Autowired
    protected TeamProblemRepository teamProblemRepository;

    /**
     * Get report for all entities in the contest
     * @param contest report contest
     * @return report in JSON
     */
    public abstract JsonArray reportAll(Contest contest);

    /**
     * Get report for a given entity
     * @param entity reported entity
     * @param contest report contest
     * @return report in JSON
     */
    public abstract JsonObject reportSingle(T entity, Contest contest);

}
