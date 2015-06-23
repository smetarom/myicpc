package com.myicpc.service.report.template;

import com.myicpc.model.security.SystemUserRole;
import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.builder.expression.MessageExpression;
import net.sf.dynamicreports.report.builder.expression.ValueExpression;
import net.sf.dynamicreports.report.definition.ReportParameters;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.SimpleFormatter;

/**
 * @author Roman Smetana
 */
public class ReportFormatter {
    public static final DateFormatter dateFormatter = new DateFormatter();
    public static final DateTimeFormatter dateTimeFormatter = new DateTimeFormatter();
    public static final SystemUserRoleFormatter systemUserRoleFormatter = new SystemUserRoleFormatter();

    public static class DateFormatter extends AbstractValueFormatter<String, Date> {
        final DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.US);

        @Override
        public String format(Date value, ReportParameters reportParameters) {
            return dateFormatter.format(value);
        }
    }

    public static class DateTimeFormatter extends AbstractValueFormatter<String, Date> {
        final DateFormat dateFormatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);

        @Override
        public String format(Date value, ReportParameters reportParameters) {
            return dateFormatter.format(value);
        }
    }

    public static class SystemUserRoleFormatter extends AbstractValueFormatter<String, SystemUserRole> {

        @Override
        public String format(SystemUserRole systemUserRole, ReportParameters reportParameters) {
            if (systemUserRole == null) {
                return "";
            }
            return new MessageExpression(systemUserRole.getUserRoleLabel().getCode()).evaluate(reportParameters);
        }
    }
}
