package com.pgbit.blogapp.service;

import java.util.UUID;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pgbit.blogapp.model.User;
import com.pgbit.blogapp.repository.IUserRepository;

@Service
public class UserService {

	private static final String USER_PHOTO_DIRECTORY = "/images/user";
	private static final String PHOTO_FILE_EXTENSION = ".jpg";

	@Inject
	private IUserRepository repository;

	@Inject
	private IFileService fileService;

	public User saveUser(User user) {
		return repository.save(user);
	}

	public User getUser(UUID userId) {
		return repository.findById(userId).orElse(null);
	}

	public boolean saveUserPhoto(String userId, MultipartFile photoFile) {
		return fileService.uploadFile(photoFile, USER_PHOTO_DIRECTORY, getUserPhotoFileName(userId));
	}

	private String getUserPhotoFileName(String userId) {
		return userId.concat(PHOTO_FILE_EXTENSION);
	}
}