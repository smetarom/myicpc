package com.myicpc.service.exception;

/**
 * User tries to access resources without required permissions
 *
 * @author Roman Smetana
 */
public class AuthenticationException extends Exception {
    private static final long serialVersionUID = -8545513890630753373L;

    public AuthenticationException() {
    }

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
