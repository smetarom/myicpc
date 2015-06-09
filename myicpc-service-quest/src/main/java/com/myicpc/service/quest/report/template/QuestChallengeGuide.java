package com.myicpc.service.quest.report.template;

import com.myicpc.model.quest.QuestChallenge;
import com.myicpc.service.report.template.ReportExpressions.LabelExpression;
import com.myicpc.service.report.template.ReportFormatter;
import com.myicpc.service.report.template.ReportTemplate;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.SubreportBuilder;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.SplitType;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static com.myicpc.service.report.template.ReportTemplate.translateText;
import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.tableOfContentsHeading;

/**
 * @author Roman Smetana
 */
public class QuestChallengeGuide {

    public QuestChallengeGuide() {
    }

    public JasperReportBuilder build(List<QuestChallenge> questChallenges) {
        JasperReportBuilder report = ReportTemplate.baseReport();

        SubreportBuilder subreport = cmp.subreport(new SubreportExpression(questChallenges))
                .setDataSource(new JREmptyDataSource());

        report.tableOfContents();
        report.detail(subreport);

        report.setDataSource(new JREmptyDataSource(questChallenges.size()));
        return report;
    }

    private class SubreportExpression extends AbstractSimpleExpression<JasperReportBuilder> {
        private List<QuestChallenge> challenges;

        public SubreportExpression(List<QuestChallenge> challenges) {
            this.challenges = challenges;
        }

        @Override
        public JasperReportBuilder evaluate(ReportParameters reportParameters) {
            int index = reportParameters.getReportRowNumber() - 1;
            QuestChallenge challenge = challenges.get(index);

            TextFieldBuilder<String> title = cmp.text(challenge.getName() + "(#" + challenge.getHashtag() + ")")
                    .setStyle(ReportTemplate.h1Style)
                    .setTableOfContentsHeading(tableOfContentsHeading());

            JasperReportBuilder report = report();
            report.title(title);
            report.detail(
                    createChallengeComponent(challenge),
                    cmp.verticalGap(10));
            return report;
        }
    }

    protected ComponentBuilder<?, ?> createChallengeComponent(QuestChallenge challenge) {

        VerticalListBuilder verticalList = cmp.verticalList();

        verticalList.add(cmp.text(new LabelExpression<>(translateText("quest.challengeGuide.hashtag"), "#" + challenge.getHashtag())));
        PointsExpression pointsExpression = new PointsExpression(challenge.getDefaultPoints(), challenge.isRequiresPhoto(), challenge.isRequiresVideo());
        verticalList.add(cmp.text(new LabelExpression<>(translateText("quest.challengeGuide.points.awarded"), pointsExpression)));
        verticalList.add(cmp.text(new LabelExpression<>(translateText("quest.challengeGuide.startDate"), challenge.getLocalStartDate(), ReportFormatter.dateTimeFormatter)));
        if (challenge.getEndDate() != null) {
            verticalList.add(cmp.text(new LabelExpression<>(translateText("quest.challengeGuide.endDate"), challenge.getLocalEndDate(), ReportFormatter.dateTimeFormatter)));
        }
        verticalList.add(cmp.text(challenge.getDescription()));
        if (challenge.getImageURL() != null) {
            try {
                URL imageURL = new URL(challenge.getImageURL());
                verticalList.add(cmp.image(imageURL).setHorizontalAlignment(HorizontalAlignment.CENTER));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        return verticalList;
    }

    private static class PointsExpression extends AbstractSimpleExpression<String> {
        private int points;
        private boolean attachPhoto;
        private boolean attachVideo;

        public PointsExpression(int points, boolean attachPhoto, boolean attachVideo) {
            this.points = points;
            this.attachPhoto = attachPhoto;
            this.attachVideo = attachVideo;
        }

        @Override
        public String evaluate(ReportParameters reportParameters) {
            StringBuilder sb = new StringBuilder();
            sb.append(translateText("quest.challengeGuide.points.x", points).evaluate(reportParameters)).append(" ");
            if (attachPhoto && attachVideo) {
                sb.append("(").append(translateText("quest.challengeGuide.points.attachPhotoOrVideo").evaluate(reportParameters)).append(")");
            } else if (attachPhoto) {
                sb.append("(").append(translateText("quest.challengeGuide.points.attachPhoto").evaluate(reportParameters)).append(")");
            } else if (attachVideo) {
                sb.append("(").append(translateText("quest.challengeGuide.points.attachVideo").evaluate(reportParameters)).append(")");
            }
            return sb.toString();
        }
    }



    public JasperReportBuilder build2(List<QuestChallenge> questChallenges) {
        JasperReportBuilder report = ReportTemplate.baseReport();

        report.tableOfContents();

        ComponentBuilder<?, ?>[] details = new ComponentBuilder[questChallenges.size()];
        int index = 0;
        for (QuestChallenge challenge : questChallenges) {
            details[index++] = createChallengeComponent(challenge);
//            report.detail(createChallengeComponent(challenge)).setDetailSplitType(SplitType.PREVENT);
        }
        report.detail(details);
//        report.setDetailSplitType(SplitType.PREVENT);

        report.setDataSource(new JRBeanCollectionDataSource(questChallenges));
        report.setDataSource(new JREmptyDataSource());
        return report;
    }

}
