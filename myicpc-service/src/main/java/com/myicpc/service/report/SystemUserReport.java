package com.myicpc.service.report;


import org.springframework.stereotype.Service;

/**
 * @author Roman Smetana
 */
@Service
public class SystemUserReport extends AbstractReport {
//    public void generateUserReport(List<SystemUser> content, OutputStream outputStream) {
//        try {
//            JRDataSource dataSource = new JRBeanCollectionDataSource(content);
//            JasperReportBuilder builder = report();
//            builder.columns(
//                    col.column("username", type.stringType()),
//                    col.column("firstname", type.stringType()),
//                    col.column("lastname", type.stringType()),
//                    col.booleanColumn("enabled")
//            );
//            builder.setDataSource(dataSource);
//            exportToPDF(builder, outputStream);
//        } catch (DRException ex) {
//            throw new ReportException(ex);
//        }
//    }
}
