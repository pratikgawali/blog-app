package com.pgbit.blogapp.exception;


/**
 * Exception corresponding to validation error.
 * 
 * @author Pratik Gawali
 *
 */
public class ValidationException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public ValidationException(String message) {
		super(message);
	}
	
	public ValidationException(Throwable cause) {
		super(cause);
	}
	
	public ValidationException(String message, Throwable cause) {
		super(message, cause);
	}
}