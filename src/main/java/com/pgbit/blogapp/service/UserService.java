package com.pgbit.blogapp.service;

import static com.pgbit.blogapp.service.storage.FileStorageParameterKeys.FILE_ID;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pgbit.blogapp.exception.FileStorageException;
import com.pgbit.blogapp.exception.TechnicalException;
import com.pgbit.blogapp.model.Role;
import com.pgbit.blogapp.model.User;
import com.pgbit.blogapp.model.UserRoles;
import com.pgbit.blogapp.repository.IRoleRepository;
import com.pgbit.blogapp.repository.IUserRepository;
import com.pgbit.blogapp.security.RolesEnum;
import com.pgbit.blogapp.service.storage.IFileStorageService;

/**
 * Service class for operations on {@link User} entity.
 * 
 * @author Pratik Gawali
 *
 */
@Service
public class UserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	@Inject
	private IUserRepository userRepository;
	
	@Inject
	private IRoleRepository roleRepository;

	@Inject
	private IFileStorageService fileStorageService;

	@Inject
	private PasswordEncoder passwordEncoder;

	/**
	 * Saves the given {@link User} with the password encoded.
	 * 
	 * @param user {@link User} instance.
	 * @return the {@link User} saved instance.
	 * @throws TechnicalException
	 */
	//TODO: make transactional
	public User saveUser(User user) throws TechnicalException {
		
		boolean isNewUser = Objects.isNull(user.getId());
	
		String password = user.getPassword();
		if (!Objects.isNull(password)) {
			String encodedPassword = passwordEncoder.encode(password);
			user.setPassword(encodedPassword);
		}
		
		// assign default role to a new user
		if (isNewUser) {
			
			Optional<Role> queryResult = roleRepository.findByName(RolesEnum.USER.getRoleName());
			queryResult.orElseThrow(()-> new TechnicalException("Given role does not exist to be assigned to a user"));
			Role role = queryResult.get();
			
			UserRoles userRoles = new UserRoles(user, role);
			user.setUserRoles(Arrays.asList(userRoles));
		}
		
		return userRepository.save(user);
	}

	/**
	 * Gets the {@link User} identified by the given user id.
	 * 
	 * @param userId id of the {@link User} to be read.
	 * @return {@link User} instance identified by the given user id.
	 */
	public User getUser(UUID userId) {

		return userRepository.findById(userId).orElse(null);
	}

	/**
	 * Saves the given image of the {@link User}.
	 * 
	 * @param userId    id of {@link User} whose image needs to be saved.
	 * @param imageFile image file of the {@link User}.
	 * @throws TechnicalException
	 */
	public void saveUserImage(UUID userId, MultipartFile imageFile) throws TechnicalException {

		User user = getUser(userId);
		if (Objects.isNull(user)) {
			LOGGER.error("User does not exist for whom the image is to be uploaded.");
			throw new TechnicalException("User does not exist for whom the image is to be uploaded.");
		}

		Map<String, Object> parameters = new HashMap<>();
		parameters.put(FILE_ID, user.getImageId());
		try {
			String imageId = fileStorageService.uploadFile(imageFile, parameters);
			user.setImageId(imageId);
			saveUser(user);
		} catch (FileStorageException e) {
			LOGGER.error("Error occurred while uploading user image file.");
			throw new TechnicalException(e);
		}
	}

	/**
	 * Deletes image file corresponding to the {@link User} from the storage.
	 * 
	 * @param userId id of {@link User} whose image needs to be deleted.
	 * @throws TechnicalException
	 */
	public void deleteUserImage(UUID userId) throws TechnicalException {

		User user = getUser(userId);

		if (Objects.isNull(user)) {
			LOGGER.error("User does not exist whose image is to be deleted.");
			throw new TechnicalException("User does not exist whose image is to be deleted.");
		}
		if (Objects.isNull(user.getImageId())) {
			LOGGER.error("No image exists to delete for the given user.");
			throw new TechnicalException("No image exists to delete for the given user.");
		}

		Map<String, Object> parameters = new HashMap<>();
		parameters.put(FILE_ID, user.getImageId());
		try {
			fileStorageService.deleteFile(parameters);
			user.setImageId(null);
			saveUser(user);
		} catch (FileStorageException e) {
			LOGGER.error("Error occurred while deleting user image file.");
			throw new TechnicalException(e);
		}
	}
}