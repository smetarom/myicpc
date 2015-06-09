package com.myicpc.service.report;

import net.sf.dynamicreports.jasper.builder.JasperConcatenatedReportBuilder;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.exception.DRException;

import java.io.OutputStream;

/**
 * @author Roman Smetana
 */
public class AbstractReportService {

    protected void exportToPDF(JasperReportBuilder builder, OutputStream outputStream) throws DRException {
        builder.toPdf(outputStream);
    }

    protected void exportToPDF(JasperConcatenatedReportBuilder builder, OutputStream outputStream) throws DRException {
        builder.toPdf(outputStream);
    }
}
