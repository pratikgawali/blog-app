package com.pgbit.blogapp.controller;

import java.security.Principal;

import javax.inject.Inject;

import org.springframework.security.access.prepost.PreAuthorize;
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
	 * Creates a new {@link User}.
	 * 
	 * @param user {@link User} details of the new user.
	 * @throws TechnicalException
	 * @throws ValidationException
	 */
	@PostMapping
	public void createUser(@RequestBody User user) throws TechnicalException, ValidationException {

		userService.createUser(user);
	}

	/**
	 * Updates the {@link User} details.
	 * 
	 * @param principal identifies currently logged in user.
	 * @param user      contains {@link User} details to be updated with.
	 * @throws ValidationException
	 * @throws TechnicalException
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	@PostMapping("/update")
	public void updateUser(Principal principal, @RequestBody User user) throws ValidationException, TechnicalException {

		String emailId = principal.getName();
		userService.updateUser(user, emailId);
	}

	/**
	 * Saves image of the logged in {@link User}.
	 * 
	 * @param principal identifies currently logged in user.
	 * @param imageFile image file to be saved.
	 * @throws TechnicalException
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	@PostMapping("/image")
	public void saveUserImage(Principal principal, @RequestParam("imageFile") MultipartFile imageFile)
			throws TechnicalException {

		String emailId = principal.getName();
		userService.saveUserImage(emailId, imageFile);

	}

	/**
	 * Deletes image of the logged in {@link User}.
	 * 
	 * @param principal identifies currently logged in user.
	 * @throws TechnicalException
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	@DeleteMapping("/image")
	public void deleteUserImage(Principal principal) throws TechnicalException {

		String emailId = principal.getName();
		userService.deleteUserImage(emailId);
	}
}