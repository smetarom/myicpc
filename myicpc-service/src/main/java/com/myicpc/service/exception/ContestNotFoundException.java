package com.myicpc.service.exception;

import com.myicpc.model.contest.Contest;

/**
 * Contest not found in MyICPC
 *
 * @author Roman Smetana
 */
public class ContestNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 3405381847410719817L;
    /**
     * contest code
     */
    private String contestCode;

    /**
     * Constructor
     *
     * @param message exception message
     * @param contestCode {@link Contest#code}
     * @param cause exception cause
     */
    public ContestNotFoundException(final String message, final String contestCode, final Throwable cause) {
        super(message, cause);
        this.contestCode = contestCode;
    }

    /**
     * Constructor
     *
     * @param message exception message
     * @param contestCode {@link Contest#code}
     */
    public ContestNotFoundException(final String message, final String contestCode) {
        super(message);
        this.contestCode = contestCode;
    }

    public String getContestCode() {
        return contestCode;
    }
}
