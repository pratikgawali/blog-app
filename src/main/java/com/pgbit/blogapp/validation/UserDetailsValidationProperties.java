package com.pgbit.blogapp.validation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Container class containing properties for user details validation.
 * 
 * @author Pratik Gawali
 *
 */
@Component
public class UserDetailsValidationProperties {

	@Value("${validation.user.firstname.length.min}")
	private Integer minFirstNameLength;

	@Value("${validation.user.firstname.length.max}")
	private Integer maxFirstNameLength;
	
	@Value("${validation.user.password.regex}")
	private String passwordRegex;

	private UserDetailsValidationProperties() {
	}

	public Integer getMinFirstNameLength() {
		return minFirstNameLength;
	}

	public Integer getMaxFirstNameLength() {
		return maxFirstNameLength;
	}

	public String getPasswordRegex() {
		return passwordRegex;
	}
}
