package com.myicpc.service.report.template;

import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.builder.expression.MessageExpression;
import net.sf.dynamicreports.report.definition.ReportParameters;

import java.util.Date;

/**
 * @author Roman Smetana
 */
public class ReportExpressions {

    public static class SimpleValueExpression<T> extends AbstractSimpleExpression<T> {
        private T value;

        public SimpleValueExpression(T value) {
            this.value = value;
        }

        @Override
        public T evaluate(ReportParameters reportParameters) {
            return value;
        }
    }

    public static class LabelExpression<T> extends AbstractSimpleExpression<String> {
        private MessageExpression label;
        private AbstractSimpleExpression<T> expression;
        private AbstractValueFormatter<?, T> formatter;

        public LabelExpression(MessageExpression label, T value) {
            this.label = label;
            this.expression = new SimpleValueExpression<>(value);
        }

        public LabelExpression(MessageExpression label, T value, AbstractValueFormatter formatter) {
            this.label = label;
            this.expression = new SimpleValueExpression<>(value);
            this.formatter = formatter;
        }

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

            System.out.println(label.evaluate(reportParameters) + " " + formattedValue);
            return String.format("%s: %s", label.evaluate(reportParameters), formattedValue);
        }
    }

    public static class DateExpression extends AbstractSimpleExpression<String> {
        private Date date;

        public DateExpression(Date date) {
            this.date = date;
        }

        @Override
        public String evaluate(ReportParameters reportParameters) {
            return ReportFormatter.dateTimeFormatter.format(date, reportParameters);
        }
    }
}
