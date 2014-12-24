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
 * @author Roman Smetana
 */
public abstract class AbstractInsightService<T extends EntityObject> {

    @Autowired
    protected TeamProblemRepository teamProblemRepository;

    public abstract JsonArray reportAll(Contest contest);

    public abstract JsonObject reportSingle(T entity, Contest contest);

}
