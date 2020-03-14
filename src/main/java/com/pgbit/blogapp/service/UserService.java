package com.pgbit.blogapp.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pgbit.blogapp.exception.FileStorageException;
import com.pgbit.blogapp.exception.TechnicalException;
import com.pgbit.blogapp.model.User;
import com.pgbit.blogapp.repository.IUserRepository;
import static com.pgbit.blogapp.service.storage.FileStorageParameterKeys.*;
import com.pgbit.blogapp.service.storage.IFileStorageService;

@Service
public class UserService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	@Inject
	private IUserRepository repository;

	@Inject
	private IFileStorageService fileStorageService;

	public User saveUser(User user) {
		return repository.save(user);
	}

	public User getUser(UUID userId) {
		return repository.findById(userId).orElse(null);
	}

	public void saveUserImage(String userId, MultipartFile imageFile) throws TechnicalException {
		
		Map<String, Object> parameters = new HashMap<>();
		parameters.put(USER_ID, userId);
		try {
			fileStorageService.uploadFile(imageFile, parameters);
		} catch (FileStorageException e) {
			LOGGER.error("Error occurred while uploading user image file.");
			throw new TechnicalException(e);
		}
	}
	
	public void deleteUserImage(String userId) throws TechnicalException {
		
		Map<String, Object> parameters = new HashMap<>();
		parameters.put(USER_ID, userId);
		try {
			fileStorageService.deleteFile(parameters);
		} catch (FileStorageException e) {
			LOGGER.error("Error occurred while deleting user image file.");
			throw new TechnicalException(e);
		}
	}
}