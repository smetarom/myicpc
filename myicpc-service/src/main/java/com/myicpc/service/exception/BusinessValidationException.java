package com.myicpc.service.exception;

/**
 * The process is in the state, which violates the rules
 *
 * @author Roman Smetana
 */
public class BusinessValidationException extends Exception {
    private static final long serialVersionUID = -6707504029306714368L;
    /**
     * Translation key
     */
    private final String messageCode;
    /**
     * Parameters provided to translation key
     */
    private final Object[] params;

    /**
     * Constructor
     *
     * @param messageCode translation key
     * @param params parameters provided to translation key
     */
    public BusinessValidationException(String messageCode, Object... params) {
        this.messageCode = messageCode;
        this.params = params;
    }

    /**
     * Constructor
     *
     * @param cause the reason why the exception is raised
     * @param messageCode translation key
     * @param params parameters provided to translation key
     */
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
