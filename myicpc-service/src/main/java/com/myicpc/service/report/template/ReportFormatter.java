package com.myicpc.service.report.template;

import com.myicpc.model.security.SystemUser;
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
 * Utility class defines convenient report formatter
 *
 * @author Roman Smetana
 */
public class ReportFormatter {
    /**
     * Default report date formatter instance
     */
    public static final DateFormatter dateFormatter = new DateFormatter();
    /**
     * Default report date formatter instance
     */
    public static final DateTimeFormatter dateTimeFormatter = new DateTimeFormatter();
    /**
     * Default report {@link SystemUser} formatter instance
     */
    public static final SystemUserRoleFormatter systemUserRoleFormatter = new SystemUserRoleFormatter();

    /**
     * Default report date formatter
     *
     * @author Roman Smetana
     */
    public static class DateFormatter extends AbstractValueFormatter<String, Date> {
        private static final long serialVersionUID = 2830480830954930698L;
        final DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.US);

        @Override
        public String format(Date value, ReportParameters reportParameters) {
            return dateFormatter.format(value);
        }
    }

    /**
     * Default report date formatter
     *
     * @author Roman Smetana
     */
    public static class DateTimeFormatter extends AbstractValueFormatter<String, Date> {
        private static final long serialVersionUID = 5873181685376959986L;
        final DateFormat dateFormatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);

        @Override
        public String format(Date value, ReportParameters reportParameters) {
            return dateFormatter.format(value);
        }
    }

    /**
     * Default report {@link SystemUser} formatter
     *
     * @author Roman Smetana
     */
    public static class SystemUserRoleFormatter extends AbstractValueFormatter<String, SystemUserRole> {
        private static final long serialVersionUID = -2758025182596046367L;

        @Override
        public String format(SystemUserRole systemUserRole, ReportParameters reportParameters) {
            if (systemUserRole == null) {
                return "";
            }
            return new MessageExpression(systemUserRole.getUserRoleLabel().getCode()).evaluate(reportParameters);
        }
    }
}
