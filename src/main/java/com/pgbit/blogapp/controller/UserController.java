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

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Inject
	private UserService userService;

	@GetMapping("/{userId}")
	public User getUser(@PathVariable(name = "userId") UUID userId) {
		return userService.getUser(userId);
	}

	@PostMapping
	public User createUser(@RequestBody User user) {
		return userService.saveUser(user);
	}

	@PostMapping("/photo")
	public void saveUserImage(@RequestParam("userId") String userId, @RequestParam("photo") MultipartFile imageFile)
			throws TechnicalException {

		userService.saveUserImage(userId, imageFile);
	}
	
	@DeleteMapping("/photo/{userId}")
	public void deleteUserImage(@PathVariable(name = "userId") String userId)
			throws TechnicalException {

		userService.deleteUserImage(userId);
	}
}