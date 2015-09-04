package com.myicpc.service.exception;

/**
 * Contest not found in MyICPC
 *
 * @author Roman Smetana
 */
public class ContestNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 3405381847410719817L;
    private String contestCode;

    public ContestNotFoundException(final String message, final String contestCode, final Throwable cause) {
        super(message, cause);
        this.contestCode = contestCode;
    }

    public ContestNotFoundException(final String message, final String contestCode) {
        super(message);
        this.contestCode = contestCode;
    }

    public ContestNotFoundException(final Throwable cause) {
        super(cause);
    }

    public String getContestCode() {
        return contestCode;
    }
}
