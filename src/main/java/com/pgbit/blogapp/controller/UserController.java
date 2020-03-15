package com.pgbit.blogapp.controller;

import java.util.UUID;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pgbit.blogapp.exception.TechnicalException;
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
	 * Gets {@link User} identified by the given user id.
	 * 
	 * @param userId id of the user.
	 * @return {@link User} instance identified by the given user id.
	 */
	@GetMapping("/{userId}")
	public User getUser(@PathVariable(name = "userId") UUID userId) {
		return userService.getUser(userId);
	}

	/**
	 * Creates a new instance of {@link User}.
	 * 
	 * @param user {@link User} instance to be saved.
	 * @return the saved {@link User} instance.
	 */
	@PostMapping
	public User createUser(@RequestBody User user) {
		return userService.saveUser(user);
	}

	/**
	 * Saves image of the {@link User} identified by the given user id.
	 * 
	 * @param userId    id of the {@link User} for whom image needs to be saved.
	 * @param imageFile image file to be saved.
	 * @throws TechnicalException
	 */
	@PostMapping("/photo")
	public void saveUserImage(@RequestParam("userId") UUID userId, @RequestParam("photo") MultipartFile imageFile)
			throws TechnicalException {

		userService.saveUserImage(userId, imageFile);
	}

	/**
	 * Deletes image of the {@link User} identified by the given user id.
	 * 
	 * @param userId id of the {@link User} for whom image needs to be deleted.
	 * @throws TechnicalException
	 */
	@DeleteMapping("/photo/{userId}")
	public void deleteUserImage(@PathVariable(name = "userId") UUID userId) throws TechnicalException {

		userService.deleteUserImage(userId);
	}
}