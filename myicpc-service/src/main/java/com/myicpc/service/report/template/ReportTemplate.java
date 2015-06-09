package com.myicpc.service.report.template;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.expression.MessageExpression;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.definition.ReportParameters;

import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

/**
 * @author Roman Smetana
 */
public class ReportTemplate {

    public static final StyleBuilder headlineStyle;
    public static final StyleBuilder h1Style;
    public static final StyleBuilder boldStyle;

    static {
        headlineStyle = stl.style().setFontSize(24);
        h1Style = stl.style().setFontSize(18);

        boldStyle = stl.style().bold();
    }

    public static JasperReportBuilder baseReport() {
        ResourceBundle reportResourceBundle = ResourceBundle.getBundle("i18n/report", Locale.US);
        JasperReportBuilder baseReport = report().
                setResourceBundle(reportResourceBundle).
                pageFooter(createFooter());
        return baseReport;
    }

    public static ComponentBuilder<?, ?> createFooter() {
        HorizontalListBuilder list = cmp.horizontalList();
        list.add(cmp.text(new ReportExpressions.DateExpression(new Date())));
        list.add(cmp.pageXofY().setHorizontalAlignment(HorizontalAlignment.CENTER));
        list.add(cmp.text(""));
        list.setStyle(stl.style().setTopBorder(stl.pen1Point()));
        return list;
    }

    public static MessageExpression translateText(String resourceKey) {
        return new MessageExpression(resourceKey);
    }

    public static MessageExpression translateText(String resourceKey, Object... arguments) {
        return new MessageExpression(resourceKey, arguments);
    }
}
