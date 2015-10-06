package com.myicpc.service.report.template;

import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.builder.expression.MessageExpression;
import net.sf.dynamicreports.report.definition.ReportParameters;

import java.util.Date;

/**
 * Utility class with various convenience expressions
 *
 * @author Roman Smetana
 */
public class ReportExpressions {

    /**
     * Evaluates to a simple value
     *
     * @param <T> type of the {@link #value}
     * @author Roman Smetana
     */
    public static class SimpleValueExpression<T> extends AbstractSimpleExpression<T> {
        private static final long serialVersionUID = 4771540764910512887L;

        private final T value;

        /**
         * Constructor
         *
         * @param value value to be returned by expression
         */
        public SimpleValueExpression(T value) {
            this.value = value;
        }

        @Override
        public T evaluate(ReportParameters reportParameters) {
            return value;
        }
    }

    /**
     * Evaluates to a string with format 'label': 'value'
     *
     * @param <T> type of the expression result
     * @author Roman Smetana
     */
    public static class LabelExpression<T> extends AbstractSimpleExpression<String> {
        private static final long serialVersionUID = 484909183460785985L;

        private final MessageExpression label;
        private final AbstractSimpleExpression<T> expression;
        private AbstractValueFormatter<?, T> formatter;

        /**
         * Constructor
         *
         * @param label expression with label value
         * @param value value of the field
         */
        public LabelExpression(MessageExpression label, T value) {
            this.label = label;
            this.expression = new SimpleValueExpression<>(value);
        }

        /**
         * Constructor
         *
         * @param label expression with label value
         * @param value value of the field
         * @param formatter formats {@code value}
         */
        public LabelExpression(MessageExpression label, T value, AbstractValueFormatter<?, T> formatter) {
            this.label = label;
            this.expression = new SimpleValueExpression<>(value);
            this.formatter = formatter;
        }

        /**
         * Constructor
         *
         * @param label expression with label value
         * @param expression expression with value of the field
         */
        public LabelExpression(MessageExpression label, AbstractSimpleExpression<T> expression) {
            this.label = label;
            this.expression = expression;
        }

        @Override
        public String evaluate(ReportParameters reportParameters) {
            Object formattedValue = expression.evaluate(reportParameters);
            if (formatter != null) {
                formattedValue = formatter.format(expression.evaluate(reportParameters), reportParameters);
            }

            return String.format("<b>%s:</b> %s", label.evaluate(reportParameters), formattedValue);
        }
    }

    /**
     * Evaluates to formatted date
     *
     * It uses {@link ReportFormatter#dateTimeFormatter}
     *
     * @author Roman Smetana
     */
    public static class DateExpression extends AbstractSimpleExpression<String> {
        private static final long serialVersionUID = 2635582289248143576L;

        private final Date date;

        /**
         * Constructor
         *
         * @param date date to be formatted
         */
        public DateExpression(Date date) {
            this.date = date;
        }

        @Override
        public String evaluate(ReportParameters reportParameters) {
            return ReportFormatter.dateTimeFormatter.format(date, reportParameters);
        }
    }
}
