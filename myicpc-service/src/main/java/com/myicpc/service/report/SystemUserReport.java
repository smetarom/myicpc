package com.myicpc.service.report;


import com.myicpc.model.security.SystemUser;
import com.myicpc.service.exception.ReportException;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.util.List;

import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

/**
 * @author Roman Smetana
 */
@Service
public class SystemUserReport extends AbstractReport {
    public void generateUserReport(List<SystemUser> content, OutputStream outputStream) {
        try {
            JRDataSource dataSource = new JRBeanCollectionDataSource(content);
            JasperReportBuilder builder = report();
            builder.columns(
                    col.column("username", type.stringType()),
                    col.column("firstname", type.stringType()),
                    col.column("lastname", type.stringType()),
                    col.booleanColumn("enabled")
            );
            builder.setDataSource(dataSource);
            exportToPDF(builder, outputStream);
        } catch (DRException ex) {
            throw new ReportException(ex);
        }
    }
}
