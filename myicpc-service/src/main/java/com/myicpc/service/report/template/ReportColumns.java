package com.myicpc.service.report.template;

import net.sf.dynamicreports.report.builder.column.BooleanColumnBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.expression.MessageExpression;
import net.sf.dynamicreports.report.constant.BooleanComponentType;
import net.sf.dynamicreports.report.definition.expression.DRIExpression;

import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

/**
 * @author Roman Smetana
 */
public class ReportColumns {
    public static TextColumnBuilder<String> stringColumn(String key, String fieldName) {
        return col.column(fieldName, type.stringType()).setTitle(new MessageExpression(key));
    }

    public static <T> TextColumnBuilder<T> expressionColumn(String key, DRIExpression<T> expression) {
        return col.column(expression).setTitle(new MessageExpression(key));
    }

    public static BooleanColumnBuilder booleanColumnYesNo(String key, String fieldName) {
        return col.booleanColumn(fieldName)
                .setTitle(new MessageExpression(key))
                .setComponentType(BooleanComponentType.TEXT_YES_NO);
    }
}
