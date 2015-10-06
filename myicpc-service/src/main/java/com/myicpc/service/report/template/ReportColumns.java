package com.myicpc.service.report.template;

import net.sf.dynamicreports.report.builder.column.BooleanColumnBuilder;
import net.sf.dynamicreports.report.builder.column.ColumnBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.expression.MessageExpression;
import net.sf.dynamicreports.report.constant.BooleanComponentType;
import net.sf.dynamicreports.report.definition.expression.DRIExpression;

import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

/**
 * Utility class for building simple report columns {@link ColumnBuilder}s
 *
 * @author Roman Smetana
 */
public class ReportColumns {
    /**
     * Creates simple string column with header translation
     *
     * @param key translation key
     * @param fieldName field name from data source
     * @return simple string column
     */
    public static TextColumnBuilder<String> stringColumn(String key, String fieldName) {
        return col.column(fieldName, type.stringType()).setTitle(new MessageExpression(key));
    }

    /**
     * Creates simple expression column with header translation
     *
     * @param key translation key
     * @param expression expression returning the value
     * @return simple expression column
     */
    public static <T> TextColumnBuilder<T> expressionColumn(String key, DRIExpression<T> expression) {
        return col.column(expression).setTitle(new MessageExpression(key));
    }

    /**
     * Creates simple boolean column with header translation
     *
     * True value is represented as 'Yes', false value as 'No'
     *
     * @param key translation key
     * @param fieldName field name from data source
     * @return simple string column
     */
    public static BooleanColumnBuilder booleanColumnYesNo(String key, String fieldName) {
        return col.booleanColumn(fieldName)
                .setTitle(new MessageExpression(key))
                .setComponentType(BooleanComponentType.TEXT_YES_NO);
    }
}
