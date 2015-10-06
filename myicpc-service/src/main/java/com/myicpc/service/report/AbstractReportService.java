package com.myicpc.service.report;

import net.sf.dynamicreports.jasper.builder.JasperConcatenatedReportBuilder;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.exception.DRException;

import java.io.OutputStream;

/**
 * Parent class for report services
 *
 * It defines the basic functionality common to all reports
 *
 * @author Roman Smetana
 */
public class AbstractReportService {

    /**
     * Creates a PDF view of the report
     *
     * It streams the PDF to {@code outputStream}
     *
     * @param builder report builder
     * @param outputStream stream with the report output
     * @throws DRException report generation failed
     */
    protected void exportToPDF(JasperReportBuilder builder, OutputStream outputStream) throws DRException {
        builder.toPdf(outputStream);
    }

    /**
     * Creates a PDF view of multiple reports concatenated together
     *
     * It streams the PDF to {@code outputStream}
     *
     * @param builder report builder
     * @param outputStream stream with the report output
     * @throws DRException report generation failed
     */
    protected void exportToPDF(JasperConcatenatedReportBuilder builder, OutputStream outputStream) throws DRException {
        builder.toPdf(outputStream);
    }
}
