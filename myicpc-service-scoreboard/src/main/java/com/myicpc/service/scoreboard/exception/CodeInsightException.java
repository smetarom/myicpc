package com.myicpc.service.scoreboard.exception;

/**
 * Exception raised when parsing and processing code insight
 *
 * @author Roman Smetana
 */
public class CodeInsightException extends Exception {
    public CodeInsightException() {
    }

    public CodeInsightException(String message) {
        super(message);
    }

    public CodeInsightException(String message, Throwable cause) {
        super(message, cause);
    }
}
