package com.myicpc.service.exception;

/**
 * Contest not found in MyICPC
 *
 * @author Roman Smetana
 */
public class ContestNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 3405381847410719817L;

    public ContestNotFoundException() {
        super();
    }

    public ContestNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ContestNotFoundException(final String message) {
        super(message);
    }

    public ContestNotFoundException(final Throwable cause) {
        super(cause);
    }
}
