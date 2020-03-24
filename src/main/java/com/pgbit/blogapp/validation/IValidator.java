package com.pgbit.blogapp.validation;

import com.pgbit.blogapp.exception.ValidationException;

/**
 * Interface to declare contract for validations.
 * 
 * @author Pratik Gawali
 *
 */
public interface IValidator {

	/**
	 * Validates the given object content.
	 * 
	 * @param obj contains datato be validated.
	 * @throws ValidationException
	 */
	void validate(Object obj) throws ValidationException;
}
