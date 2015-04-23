package com.myicpc.master.exception;

/**
 * @author Roman Smetana
 */
public class WebServiceException extends Exception {
    public WebServiceException() {
    }

    public WebServiceException(String message) {
        super(message);
    }

    public WebServiceException(Throwable cause) {
        super(cause);
    }

    public WebServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
