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

	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
			throws Exception {

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
					authenticationRequest.getPassword()));
		} catch (BadCredentialsException e) {
			LOGGER.error("Incorrect username or password");
			throw new Exception("Incorrect username or password", e);
		}
		
		UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
		String jwt = jwtUtil.generateToken(userDetails);
		
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
}
