package com.myicpc.service.scoreboard.insight

import com.myicpc.commons.utils.FormatUtils
import com.myicpc.model.contest.Contest
import com.myicpc.model.editActivity.CodeInsightActivity
import com.myicpc.model.eventFeed.Language
import com.myicpc.model.eventFeed.Problem
import com.myicpc.model.eventFeed.Team
import com.myicpc.repository.editActivity.CodeInsightActivityRepository
import com.myicpc.repository.eventFeed.LanguageRepository
import com.myicpc.repository.eventFeed.ProblemRepository
import com.myicpc.repository.eventFeed.TeamRepository
import com.myicpc.service.scoreboard.exception.CodeInsightException
import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.StringUtils
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer

import static org.mockito.Matchers.*
import static org.mockito.Mockito.when

/**
 * @author Roman Smetana
 */
class CodeInsightServiceTest extends GroovyTestCase {
    private Contest contest

    @Mock
    private LanguageRepository languageRepository;

    @Mock
    private ProblemRepository problemRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private CodeInsightActivityRepository codeInsightActivityRepository;

    @InjectMocks
    private CodeInsightService codeInsightService

    void setUp() {
        MockitoAnnotations.initMocks(this);
        contest = new Contest(id: 13L)
    }

    void testProcessCodeInsightResource() {
        List<CodeInsightActivity> activities = new ArrayList<>();
        when(codeInsightActivityRepository.save(any(CodeInsightActivity.class))).then(new Answer<Object>() {
            @Override
            Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                Object answer = invocationOnMock.getArgumentAt(0, Object.class);
                activities.add(answer)
                return answer
            }
        })

        when(teamRepository.findBySystemIdAndContest(anyLong(), any(Contest.class))).then(new Answer<Object>() {
            @Override
            Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                return new Team(systemId: invocationOnMock.getArgumentAt(0, Long.class))
            }
        })

        when(problemRepository.findByCodeIgnoreCaseAndContest(anyString(), any(Contest.class))).then(new Answer<Object>() {
            @Override
            Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                return new Problem(code: invocationOnMock.getArgumentAt(0, String.class))
            }
        })

        when(languageRepository.findByNameIgnoreCase(anyString())).then(new Answer<Object>() {
            @Override
            Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                return new Language(name: invocationOnMock.getArgumentAt(0, String.class))
            }
        })

        String response = readFileToString("codeInsight/codeInsight_correct.txt")
        codeInsightService.processCodeInsightResource(response, contest)

        assert activities.size() == 2
        CodeInsightActivity elem = activities.get(0)
        assert elem.externalId == 213
        assert elem.modifyTime == 80
        assert elem.team.systemId == 56
        assert elem.problem.code == "A"
        assert elem.language.name == "C++"
        assert elem.lineCount == 49
        assert elem.diffLineCount == 10
        assert elem.fileSize == 4277

        elem = activities.get(1)
        assert elem.externalId == 214
        assert elem.modifyTime == 84
        assert elem.team.systemId == 84
        assert elem.problem.code == "B"
        assert elem.language.name == "C++"
        assert elem.lineCount == 23
        assert elem.diffLineCount == 15
        assert elem.fileSize == 1547
    }

    void testProcessCodeInsightResource_nullStringResponse() {
        codeInsightService.processCodeInsightResource(null, contest)
    }

    void testProcessCodeInsightResource_emptyStringResponse() {
        codeInsightService.processCodeInsightResource("", contest)
    }

    void testProcessCodeInsightResource_emptyResponse() {
        codeInsightService.processCodeInsightResource("[]", contest)
    }

    void testProcessCodeInsightResource_invalidResponse() {
        String response = readFileToString("codeInsight/codeInsight_invalidJson.txt")
        shouldFail(CodeInsightException) {
            codeInsightService.processCodeInsightResource(response, contest)
        }
    }

    private String readFileToString(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            // TODO logging
            return null;
        }
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource(filePath).getFile());
            return FileUtils.readFileToString(file, FormatUtils.DEFAULT_ENCODING)
        } catch (IOException ex) {
            // TODO logging
            return null;
        }
    }
}
