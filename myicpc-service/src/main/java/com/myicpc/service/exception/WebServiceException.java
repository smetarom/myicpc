package com.myicpc.service.exception;

/**
 * Communication with external server failed
 * 
 * @author Roman Smetana
 */
public class WebServiceException extends Exception {
	private static final long serialVersionUID = -7637516760144228068L;

	public WebServiceException() {
		super();
	}

	public WebServiceException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public WebServiceException(final String message) {
		super(message);
	}

	public WebServiceException(final Throwable cause) {
		super(cause);
	}
}
