package com.myicpc.service.webSevice;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.myicpc.commons.adapters.JSONAdapter;
import com.myicpc.commons.utils.FormatUtils;
import com.myicpc.dto.TranslationDto;
import com.myicpc.model.contest.Contest;
import com.myicpc.service.exception.WebServiceException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

/**
 * Service communicating with CM web services related to contest
 *
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

    /**
     * Gets the contest details from CM
     *
     * @param contest contest to get details
     * @return JSON representation of contest detail
     * @throws IOException communication error
     * @throws WebServiceException response is not valid
     */
    public String getContestDetailFromCM(final Contest contest) throws IOException, WebServiceException {
        String json = (String) connectCM("/ws/myicpc/contest/" + contest.getCode() + "/details", contest);
        JsonObject responseParent = new JsonParser().parse(json).getAsJsonObject();
        Set<Map.Entry<String, JsonElement>> children = responseParent.entrySet();
        if (children.size() == 1) {
            JsonObject response = children.iterator().next().getValue().getAsJsonObject();
            JSONAdapter jsonAdapter = new JSONAdapter(response);
            response.addProperty("hashtag", FormatUtils.clearHashtag(jsonAdapter.getString("hashtag")));

            String timeZoneName = jsonAdapter.getString("timeZone");
            if (StringUtils.isNotEmpty(timeZoneName)) {
                TimeZone timeZone = TimeZone.getTimeZone(timeZoneName);
                response.addProperty("offset", timeZone.getRawOffset() / 60000);
            }
            response.addProperty("startDate", jsonAdapter.getString("start") + " 00:00:00");
            response.addProperty("isWorldFinals", jsonAdapter.getString("key", "").contains("World-Finals"));
            return response.toString();
        }
        throw new WebServiceException("No valid response on CM WD contest detail.");
    }

}
