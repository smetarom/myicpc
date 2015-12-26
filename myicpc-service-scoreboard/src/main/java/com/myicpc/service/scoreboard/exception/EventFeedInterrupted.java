package com.myicpc.service.scoreboard.exception;

/**
 * Exception, when the feed processing is interrupted
 *
 * @author Roman Smetana
 */
public class EventFeedInterrupted extends Exception {
    private static final long serialVersionUID = -2183095227586392376L;

    public EventFeedInterrupted(String message) {
        super(message);
    }
}
