package com.pgbit.blogapp.authentication.jwt.model;

/**
 * Authentication request containing user credentials.
 * 
 * @author Pratik Gawali
 *
 */
public class AuthenticationRequest {

	private String emailId;
	private String password;

	public AuthenticationRequest() {
	}

	public AuthenticationRequest(String emailId, String password) {
		this.emailId = emailId;
		this.password = password;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
