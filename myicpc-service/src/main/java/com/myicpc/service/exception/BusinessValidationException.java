package com.myicpc.service.exception;

import javax.validation.ValidationException;

/**
 * @author Roman Smetana
 */
public class BusinessValidationException extends Exception {
    private String messageCode;
    private Object[] params;

    public BusinessValidationException(String messageCode, Object... params) {
        this.messageCode = messageCode;
        this.params = params;
    }

    public BusinessValidationException(Throwable cause, String messageCode, Object... params) {
        super(cause);
        this.messageCode = messageCode;
        this.params = params;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public Object[] getParams() {
        return params;
    }
}
