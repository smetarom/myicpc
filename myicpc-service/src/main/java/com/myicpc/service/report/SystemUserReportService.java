package com.myicpc.service.report;


import com.myicpc.model.security.SystemUser;
import com.myicpc.model.security.SystemUserRole;
import com.myicpc.service.exception.ReportException;
import com.myicpc.service.report.template.ReportColumns;
import com.myicpc.service.report.template.ReportFormatter;
import com.myicpc.service.report.template.ReportTemplate;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.builder.expression.MessageExpression;
import net.sf.dynamicreports.report.constant.BooleanComponentType;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.myicpc.service.report.template.ReportTemplate.translateText;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;

/**
 * @author Roman Smetana
 */
@Service
public class SystemUserReportService extends AbstractReportService {
    public void generateUserReport(List<SystemUser> content, OutputStream outputStream) {
        try {
            JRDataSource dataSource = new JRBeanCollectionDataSource(content);
            JasperReportBuilder builder = ReportTemplate.baseReport();
            builder.title(cmp.text(translateText("system-user.overview.title")).setStyle(ReportTemplate.headlineStyle));
            builder.columns(
                    ReportColumns.stringColumn("system-user.overview.header.username", "username"),
                    ReportColumns.stringColumn("system-user.overview.header.firstname", "firstname"),
                    ReportColumns.stringColumn("system-user.overview.header.lastname", "lastname"),
                    ReportColumns.booleanColumnYesNo("system-user.overview.header.enabled", "enabled"),
                    ReportColumns.expressionColumn("system-user.overview.header.roles", new SystemUserRoleListExpression())
            );
            builder.fields(
                field("roles", List.class)
            );
            builder.setDataSource(dataSource);
            exportToPDF(builder, outputStream);
        } catch (DRException ex) {
            throw new ReportException(ex);
        }
    }

    private class SystemUserRoleListExpression extends AbstractSimpleExpression<String> {
        @Override
        public String evaluate(ReportParameters reportParameters) {
            List<SystemUserRole> roles = reportParameters.getValue("roles");
            List<String> roleNames = new ArrayList<>();
            for (SystemUserRole role : roles) {
                try {
                    roleNames.add(translateText(role.getUserRoleLabel().getCode()).evaluate(reportParameters));
                } catch (Exception ex) {
                    roleNames.add(role.getUserRoleLabel().getLabel());
                }
            }
            return StringUtils.join(roleNames, '\n');
        }
    }
}
