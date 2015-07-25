package com.myicpc.service.quest.report

import com.myicpc.commons.utils.TimeUtils
import com.myicpc.model.contest.Contest
import com.myicpc.model.quest.QuestChallenge
import org.mockito.InjectMocks
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

/**
 * Unit tests for {@link QuestReportService}
 *
 * @author Roman Smetana
 */
class QuestReportServiceTest extends GroovyTestCase {
    @InjectMocks
    private QuestReportService questReportService;

    @Override
    protected void setUp() throws Exception {
        MockitoAnnotations.initMocks(this)
    }

    void testDownloadQuestChallengesGuide() {
        def contest = new Contest(id: 1L)
        def challenge1 = new QuestChallenge(id: 1L,
                name: "Quest 1",
                defaultPoints: 10,
                startDate: TimeUtils.getDate(2014, 6, 15),
                endDate: TimeUtils.getDate(2014, 6, 21),
                imageURL: "http://localhost/image.png",
                hashtagPrefix: "Quest",
                hashtagSuffix: "1",
                requiresPhoto: true,
                requiresVideo: true,
                contest: contest)
        def challenge2 = new QuestChallenge(id: 2L,
                name: "Quest 2",
                defaultPoints: 10,
                startDate: TimeUtils.getDate(2014, 6, 15),
                endDate: TimeUtils.getDate(2014, 6, 16),
                hashtagPrefix: "Quest",
                hashtagSuffix: "2",
                requiresPhoto: true,
                requiresVideo: true,
                contest: contest)

        questReportService.downloadQuestChallengesGuide([challenge1, challenge2], Mockito.mock(OutputStream.class), true)
    }

    void testDownloadQuestChallengesGuideNullEndDate() {
        def contest = new Contest(id: 1L)
        def challenge2 = new QuestChallenge(id: 1L,
                name: "Quest 1",
                defaultPoints: 10,
                startDate: TimeUtils.getDate(2014, 6, 15),
                endDate: null,
                hashtagPrefix: "Quest",
                hashtagSuffix: "1",
                requiresPhoto: true,
                requiresVideo: true,
                contest: contest)

        questReportService.downloadQuestChallengesGuide([challenge2], Mockito.mock(OutputStream.class), true)
    }

    void testDownloadQuestChallengesGuideNoAttachment() {
        def contest = new Contest(id: 1L)
        def challenge3 = new QuestChallenge(id: 1L,
                name: "Quest 1",
                defaultPoints: 10,
                startDate: TimeUtils.getDate(2014, 6, 15),
                endDate: null,
                hashtagPrefix: "Quest",
                hashtagSuffix: "1",
                contest: contest)

        questReportService.downloadQuestChallengesGuide([challenge3], Mockito.mock(OutputStream.class), true)
    }
}
