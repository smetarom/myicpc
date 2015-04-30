package com.myicpc.master.exception;

/**
 * Error during processing the event feed
 *
 * @author Roman Smetana
 */
public class EventFeedException extends Exception {
    private static final long serialVersionUID = 3405381847410719817L;

    public EventFeedException() {
        super();
    }

    public EventFeedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public EventFeedException(final String message) {
        super(message);
    }

    public EventFeedException(final Throwable cause) {
        super(cause);
    }
}
