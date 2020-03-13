package com.pgbit.blogapp.exception;

public class TechnicalException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public TechnicalException(String message) {
		super(message);
	}
	
	public TechnicalException(Throwable cause) {
		super(cause);
	}
	
	public TechnicalException(String message, Throwable cause) {
		super(message, cause);
	}
}