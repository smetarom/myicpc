package com.myicpc.service.exception;

/**
 * An error occurred during the report generation in MyICPC
 *
 * @author Roman Smetana
 */
public class ReportException extends RuntimeException {
    private static final long serialVersionUID = 3405381847410719817L;

    public ReportException() {
        super();
    }

    public ReportException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ReportException(final String message) {
        super(message);
    }

    public ReportException(final Throwable cause) {
        super(cause);
    }
}
