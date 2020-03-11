package com.pgbit.blogapp.service;

import java.util.UUID;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pgbit.blogapp.model.User;
import com.pgbit.blogapp.repository.IUserRepository;

@Service
public class UserService {
	
	private static final String USER_PHOTO_DIRECTORY = "\\resources\\photo";

	@Inject
	private IUserRepository repository;
	
	@Inject
	private FileService fileService;

	public User saveUser(User user) {
		return repository.save(user);
	}

	public User getUser(UUID userId) {
		return repository.findById(userId).orElse(null);
	}

	public boolean saveUserPhoto(MultipartFile photoFile) {
		fileService.setDirectoryPathName(USER_PHOTO_DIRECTORY);
		return fileService.saveFile(photoFile);
	}
}
