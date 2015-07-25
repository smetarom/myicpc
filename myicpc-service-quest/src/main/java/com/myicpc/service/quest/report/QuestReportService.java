package com.myicpc.service.quest.report;

import com.myicpc.model.quest.QuestChallenge;
import com.myicpc.service.quest.report.template.QuestChallengeGuide;
import com.myicpc.service.report.AbstractReportService;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.exception.DRException;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.util.List;

/**
 * Service responsible for Quest reports/exports
 *
 * @author Roman Smetana
 */
@Service
public class QuestReportService extends AbstractReportService {

    /**
     * Generates a PDF with a detail view on every challenge from {@code questChallenges}
     *
     * @param questChallenges list of exported challenges
     * @param outputStream    export output
     * @param generateTOC     if generate table of content
     * @throws DRException error during export generation
     */
    public void downloadQuestChallengesGuide(List<QuestChallenge> questChallenges, OutputStream outputStream, boolean generateTOC) throws DRException {
        exportToPDF(reportQuestChallengesGuide(questChallenges, generateTOC), outputStream);
    }

    private JasperReportBuilder reportQuestChallengesGuide(List<QuestChallenge> questChallenges, boolean generateTOC) {
        QuestChallengeGuide questChallengeGuide = new QuestChallengeGuide(generateTOC);
        return questChallengeGuide.build(questChallenges);
    }
}
