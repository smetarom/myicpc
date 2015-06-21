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
 * @author Roman Smetana
 */
@Service
public class QuestReportService extends AbstractReportService {

    public void downloadQuestChallengesGuide(List<QuestChallenge> questChallenges, OutputStream outputStream) throws DRException {
        exportToPDF(reportQuestChallengesGuide(questChallenges), outputStream);
    }

    public JasperReportBuilder reportQuestChallengesGuide(List<QuestChallenge> questChallenges) {
        QuestChallengeGuide questChallengeGuide = new QuestChallengeGuide();
        return questChallengeGuide.build(questChallenges);

    }
}
