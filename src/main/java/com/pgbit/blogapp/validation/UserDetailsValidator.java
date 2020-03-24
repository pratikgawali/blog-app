package com.pgbit.blogapp.validation;

import java.util.Objects;

import javax.inject.Inject;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;

import com.pgbit.blogapp.exception.ValidationException;
import com.pgbit.blogapp.model.User;
import com.pgbit.blogapp.service.UserEntityService;

/**
 * Validation class for user details.
 * 
 * @author Pratik Gawali
 *
 */
@Component(UserDetailsValidator.BEAN_NAME)
public class UserDetailsValidator implements IValidator {

	public static final String BEAN_NAME = "UserDetailsValidator";

	@Inject
	private UserEntityService userEntityService;

	@Inject
	private UserDetailsValidationProperties properties;

	/**
	 * Validates {@link User} details.
	 * 
	 * @param userDetails details of {@link User}.
	 */
	@Override
	public void validate(Object userDetails) throws ValidationException {

		User user = (User) userDetails;

		boolean isNewUser = userEntityService.isNewUser(user.getEmailId());

		String firstName = user.getFirstName();
		if (isValidationRelevant(isNewUser, firstName)) {
			validateFirstName(firstName);
		}

		// email id validation is relevant for both create & update scenario
		validateEmailId(user.getEmailId());

		String password = user.getPassword();
		if (isValidationRelevant(isNewUser, password)) {
			validatePassword(password);
		}
	}

	/**
	 * Validates the first name of the user.
	 * 
	 * @param firstName
	 * @throws ValidationException
	 */
	private void validateFirstName(String firstName) throws ValidationException {

		if (Objects.isNull(firstName) || firstName.isEmpty()) {
			throw new ValidationException("First name of a user is mandatory.");
		}

		int len = firstName.length();
		if (len < properties.getMinFirstNameLength() || len > properties.getMaxFirstNameLength()) {
			throw new ValidationException("Invalid length of the first name.");
		}
	}

	/**
	 * Validates email id of the user.
	 * 
	 * @param emailId
	 * @throws ValidationException
	 */
	private void validateEmailId(String emailId) throws ValidationException {

		if (Objects.isNull(emailId)) {
			throw new ValidationException("Email id is mandatory.");
		}

		if (!EmailValidator.getInstance().isValid(emailId)) {
			throw new ValidationException("Invalid email address.");
		}
	}

	/**
	 * Validates password of the user.
	 * 
	 * @param password
	 * @throws ValidationException
	 */
	private void validatePassword(String password) throws ValidationException {

		if (Objects.isNull(password)) {
			throw new ValidationException("Password is mandatory.");
		}

		if (!password.matches(properties.getPasswordRegex())) {
			throw new ValidationException("Invalid password format.");
		}
	}

	/**
	 * Validation is only relevant when the user is new or non null fields value for
	 * existing user.
	 * 
	 * @param isNewUser to identify if the user is new.
	 * @param value     field value which will be validated.
	 * @return true if validation is relevant, otherwise false.
	 */
	private boolean isValidationRelevant(boolean isNewUser, Object value) {
		return isNewUser || (!isNewUser && !Objects.isNull(value));
	}
}
