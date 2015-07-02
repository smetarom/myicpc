package com.myicpc.service.webSevice;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.myicpc.commons.adapters.JSONAdapter;
import com.myicpc.commons.utils.FormatUtils;
import com.myicpc.dto.TranslationDto;
import com.myicpc.model.contest.Contest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.TimeZone;

/**
 * @author Roman Smetana
 */
@Service
public class ContestWSService extends AbstractWSService {

    /**
     * Ping the CM contest and get connectivity
     *
     * @return translation code and alternative text
     * @throws IOException communication error
     */
    public TranslationDto pingCMContest(final Contest contest) throws IOException {
        String json = (String) connectCM("/ws/myicpc/contest/" + contest.getCode() + "/ping", contest);
        JsonObject response = new JsonParser().parse(json).getAsJsonObject();
        JSONAdapter jsonAdapter = new JSONAdapter(response);
        return new TranslationDto("contestAdmin.wsCheck." + jsonAdapter.getString("code"), jsonAdapter.getString("message"));
    }

    public String getContestDetailFromCM(final Contest contest) throws IOException {
        String json = (String) connectCM("/ws/myicpc/contest/" + contest.getCode() + "/details", contest);
        JsonObject response = new JsonParser().parse(json).getAsJsonObject();
        JSONAdapter jsonAdapter = new JSONAdapter(response);
        response.addProperty("hashtag", FormatUtils.clearHashtag(jsonAdapter.getString("hashtag")));

        TimeZone timeZone = TimeZone.getTimeZone(jsonAdapter.getString("timeZone"));
        response.addProperty("offset", timeZone.getRawOffset() / 60000);

        response.addProperty("startDate", jsonAdapter.getString("start") + " 00:00:00");
        response.addProperty("isWorldFinals", jsonAdapter.getString("key", "").contains("World-Finals"));
        return response.toString();
    }

}
