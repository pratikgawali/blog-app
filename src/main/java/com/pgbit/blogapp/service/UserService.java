package com.pgbit.blogapp.service;

import java.util.UUID;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.pgbit.blogapp.model.User;
import com.pgbit.blogapp.repository.IUserRepository;

@Service
public class UserService {

	@Inject
	private IUserRepository repository;
	
	public User saveUser(User user) {
		return repository.save(user);
	}
	
	public User getUser(UUID userId) {
		return repository.findById(userId).orElse(null);
	}
}
