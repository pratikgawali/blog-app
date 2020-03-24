package com.pgbit.blogapp.controller;

import java.security.Principal;
import java.util.Objects;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pgbit.blogapp.exception.TechnicalException;
import com.pgbit.blogapp.exception.ValidationException;
import com.pgbit.blogapp.model.User;
import com.pgbit.blogapp.service.UserEntityService;
import com.pgbit.blogapp.service.UserService;

/**
 * Controller for handling requests related to {@link User}.
 * 
 * @author Pratik Gawali
 *
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

	@Inject
	private UserService userService;

	@Inject
	private UserEntityService userEntityService;

	/**
	 * Gets {@link User} identified by the given email id.
	 * 
	 * @param emailId email id of the user.
	 * @return {@link User} instance identified by the given email id.
	 * @throws TechnicalException
	 */
	@GetMapping("")
	public User getUser(@RequestParam("emailId") String emailId) throws TechnicalException {
		return userService.getUser(emailId);
	}

	/**
	 * Saves the given {@link User} instance. User must be authorized to update
	 * already saved user details.
	 * 
	 * @param user {@link User} instance to be saved.
	 * @throws TechnicalException
	 * @throws ValidationException
	 */
	@PostMapping
	public ResponseEntity<String> saveUser(Authentication authentication, @RequestBody User user)
			throws TechnicalException, ValidationException {

		// for user details updation, the user must be authenticated
		if (!userEntityService.isNewUser(user.getEmailId())) {
			if (Objects.isNull(authentication) || !authentication.isAuthenticated()
					|| !authentication.getName().equals(user.getEmailId())) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			}
		}

		userService.saveUser(user);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	/**
	 * Saves image of the logged in {@link User}.
	 * 
	 * @param imageFile image file to be saved.
	 * @throws TechnicalException
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	@PostMapping("/photo")
	public void saveUserImage(Principal principal, @RequestParam("photo") MultipartFile imageFile)
			throws TechnicalException {

		String emailId = principal.getName();
		userService.saveUserImage(emailId, imageFile);

	}

	/**
	 * Deletes image of the logged in {@link User}.
	 * 
	 * @throws TechnicalException
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	@DeleteMapping("/photo")
	public void deleteUserImage(Principal principal) throws TechnicalException {

		String emailId = principal.getName();
		userService.deleteUserImage(emailId);
	}
}