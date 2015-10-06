package com.myicpc.service.report.template;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.builder.expression.MessageExpression;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.Markup;
import net.sf.dynamicreports.report.constant.VerticalAlignment;

import java.awt.*;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;

/**
 * Report utility class, which offers default styles and report operations
 *
 * @author Roman Smetana
 */
public class ReportTemplate {
    /**
     * Default style
     */
    public static final StyleBuilder rootStyle;
    /**
     * Report headline style
     */
    public static final StyleBuilder headlineStyle;
    /**
     * Report level 1 headline style
     */
    public static final StyleBuilder h1Style;
    /**
     * Bold text
     */
    public static final StyleBuilder boldStyle;
    /**
     * Default report column style
     */
    public static final StyleBuilder columnStyle;
    /**
     * Default report column header style
     */
    public static final StyleBuilder columnTitleStyle;

    /**
     * Initialize styles
     */
    static {
        rootStyle = stl.style().setPadding(2);
        headlineStyle = stl.style().setFontSize(24).setBottomPadding(20);
        h1Style = stl.style().setFontSize(18);

        boldStyle = stl.style().bold();

        columnStyle = stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE);
        columnTitleStyle = stl.style(columnStyle)
                .setBorder(stl.pen1Point())
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.TOP)
                .setBackgroundColor(Color.LIGHT_GRAY)
                .bold();
    }

    /**
     * Creates base report template
     *
     * @return base report template
     */
    public static JasperReportBuilder baseReport() {
        JasperReportBuilder baseReport = baseSubreport()
                .pageFooter(createFooter());
        return baseReport;
    }

    /**
     * Creates base sub-report template
     *
     * @return base sub-report template
     */
    public static JasperReportBuilder baseSubreport() {
        ResourceBundle reportResourceBundle = ResourceBundle.getBundle("i18n/report", Locale.US);
        JasperReportBuilder baseReport = report()
                .setResourceBundle(reportResourceBundle)
                .setColumnStyle(columnStyle)
                .setColumnTitleStyle(columnTitleStyle)
                .highlightDetailEvenRows();
        return baseReport;
    }

    /**
     * Creates default report footer
     *
     * It contains the generation date and indicator page X of Y
     *
     * @return default report footer
     */
    public static ComponentBuilder<?, ?> createFooter() {
        HorizontalListBuilder list = cmp.horizontalList();
        list.add(cmp.text(new ReportExpressions.DateExpression(new Date())));
        list.add(cmp.pageXofY().setHorizontalAlignment(HorizontalAlignment.CENTER));
        list.add(cmp.text(""));
        list.setStyle(stl.style().setTopBorder(stl.pen1Point()));
        return list;
    }

    /**
     * Translates {@code resourceKey}
     *
     * @param resourceKey translation key
     * @return expression with translated message
     */
    public static MessageExpression translateText(String resourceKey) {
        return new MessageExpression(resourceKey);
    }

    /**
     * Translates {@code resourceKey} with parameters {@code arguments}
     *
     * @param resourceKey translation key
     * @param arguments translation parameters
     * @return expression with translated message
     */
    public static MessageExpression translateText(String resourceKey, Object... arguments) {
        return new MessageExpression(resourceKey, arguments);
    }

    /**
     * Creates a label-value expression
     *
     * @param key translation key
     * @param value value
     * @param <T> type of {@value}
     * @return label-value expression
     */
    public static <T> TextFieldBuilder<String> labeledText(String key, T value) {
        TextFieldBuilder<String> builder = cmp.text(new ReportExpressions.LabelExpression<>(translateText(key), value));
        builder.setStyle(stl.style().setMarkup(Markup.HTML));
        return builder;
    }

    /**
     * Creates a label-value expression
     *
     * @param key translation key
     * @param value value
     * @param formatter value formatter
     * @param <T> type of {@value}
     * @return label-value expression
     */
    public static <T> TextFieldBuilder<String> labeledText(String key, T value, AbstractValueFormatter<?, T> formatter) {
        TextFieldBuilder<String> builder = cmp.text(new ReportExpressions.LabelExpression<>(translateText(key), value, formatter));
        builder.setStyle(stl.style().setMarkup(Markup.HTML));
        return builder;
    }

    /**
     * Creates a label-value expression
     *
     * @param key translation key
     * @param expression value expression
     * @param <T> type of {@value}
     * @return label-value expression
     */
    public static <T> TextFieldBuilder<String> labeledText(String key, AbstractSimpleExpression<T> expression) {
        TextFieldBuilder<String> builder = cmp.text(new ReportExpressions.LabelExpression<>(translateText(key), expression));
        builder.setStyle(stl.style().setMarkup(Markup.HTML));
        return builder;
    }
}
