package com.pgbit.blogapp.authentication.jwt.model;

/**
 * JWT authentication response.
 * 
 * @author Pratik Gawali
 *
 */
public class AuthenticationResponse {

	private String jwt;

	public AuthenticationResponse(String jwt) {
		this.jwt = jwt;
	}

	public String getJwt() {
		return jwt;
	}
}
