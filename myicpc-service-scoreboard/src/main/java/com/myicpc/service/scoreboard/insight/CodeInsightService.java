package com.myicpc.service.scoreboard.insight;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.myicpc.commons.adapters.JSONAdapter;
import com.myicpc.model.codeInsight.CodeInsightActivity;
import com.myicpc.model.contest.Contest;
import com.myicpc.repository.codeInsight.CodeInsightActivityRepository;
import com.myicpc.repository.eventFeed.LanguageRepository;
import com.myicpc.repository.eventFeed.ProblemRepository;
import com.myicpc.repository.eventFeed.TeamRepository;
import com.myicpc.service.scoreboard.exception.CodeInsightException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Roman Smetana
 */
@Service
@Transactional
public class CodeInsightService {
    private static final Logger logger = LoggerFactory.getLogger(CodeInsightService.class);

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private CodeInsightActivityRepository codeInsightActivityRepository;


    public void processCodeInsightResource(final String codeInsightResponse, final Contest contest) throws CodeInsightException {

        if (StringUtils.isEmpty(codeInsightResponse) || "[]".equals(codeInsightResponse)) {
            // skip if result is empty
            return;
        }

        try {
            JsonArray arr = new JsonParser().parse(codeInsightResponse).getAsJsonArray();
            // parse each edit activity record
            for (JsonElement e : arr) {
                try {
                    // JsonObject object = e.getAsJsonObject();
                    JSONAdapter recordAdapter = new JSONAdapter(e);
                    Long externalId = recordAdapter.getLong("id");

                    CodeInsightActivity codeInsightActivity = codeInsightActivityRepository.findByExternalId(externalId);
                    if (codeInsightActivity != null) {
                        continue;
                    }
                    codeInsightActivity = new CodeInsightActivity();
                    codeInsightActivity.setExternalId(externalId);
                    codeInsightActivity.setDiffLineCount(recordAdapter.getInteger("diff_line_count"));
                    codeInsightActivity.setLineCount(recordAdapter.getInteger("line_count"));
                    codeInsightActivity.setFileSize(recordAdapter.getInteger("file_size_bytes"));
                    codeInsightActivity.setModifyTime(recordAdapter.getInteger("time"));
                    codeInsightActivity.setLanguage(languageRepository.findByNameIgnoreCase(recordAdapter.getString("language")));
                    codeInsightActivity.setTeam(teamRepository.findBySystemIdAndContest(recordAdapter.getLong("team_id"), contest));
                    codeInsightActivity.setProblem(problemRepository.findByCodeIgnoreCaseAndContest(recordAdapter.getString("problem_id"), contest));

                    codeInsightActivityRepository.save(codeInsightActivity);
                } catch (Throwable ex) {
                    logger.warn("Code insight was not processed." + codeInsightResponse, ex);
                }
            }
        } catch (JsonSyntaxException ex) {
            throw new CodeInsightException("Invalid code insight response: \n" + codeInsightResponse, ex);
        }
    }
}
