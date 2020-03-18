package com.pgbit.blogapp.authentication.jwt.controller;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pgbit.blogapp.authentication.jwt.model.AuthenticationRequest;
import com.pgbit.blogapp.authentication.jwt.model.AuthenticationResponse;
import com.pgbit.blogapp.authentication.jwt.util.JwtUtil;
import com.pgbit.blogapp.authentication.service.CustomUserDetailsService;

/**
 * Controller class for handling requests related to authentication, like
 * generating a JWT token.
 * 
 * @author Pratik Gawali
 *
 */
@RestController
@RequestMapping
public class AuthenticationController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);

	@Inject
	private AuthenticationManager authenticationManager;

	@Inject
	private CustomUserDetailsService userDetailsService;

	@Inject
	private JwtUtil jwtUtil;

	/**
	 * Creates JWT authentication token by verifying the credentials in the given
	 * {@link AuthenticationRequest} instance.
	 * 
	 * @param authenticationRequest contains user credentials
	 * @return JWT token
	 * @throws Exception
	 */
	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
			throws Exception {

		String username = authenticationRequest.getEmailId();
		
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,
					authenticationRequest.getPassword()));
		} catch (BadCredentialsException e) {
			LOGGER.error("Incorrect username or password");
			throw new Exception("Incorrect username or password", e);
		}

		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		String jwt = jwtUtil.generateToken(userDetails);

		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
}
