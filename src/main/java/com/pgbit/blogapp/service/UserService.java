package com.pgbit.blogapp.service;

import static com.pgbit.blogapp.service.storage.FileStorageParameterKeys.FILE_ID;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pgbit.blogapp.exception.FileStorageException;
import com.pgbit.blogapp.exception.TechnicalException;
import com.pgbit.blogapp.exception.ValidationException;
import com.pgbit.blogapp.model.Role;
import com.pgbit.blogapp.model.User;
import com.pgbit.blogapp.model.UserRoles;
import com.pgbit.blogapp.repository.IRoleRepository;
import com.pgbit.blogapp.repository.IUserRepository;
import com.pgbit.blogapp.security.RolesEnum;
import com.pgbit.blogapp.service.storage.IFileStorageService;
import com.pgbit.blogapp.validation.IValidator;
import com.pgbit.blogapp.validation.UserDetailsValidator;

/**
 * Service class for operations related to user.
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
	IRoleRepository roleRepository;

	@Inject
	private UserEntityService userEntityService;

	@Inject
	private IFileStorageService fileStorageService;

	@Inject
	private PasswordEncoder passwordEncoder;

	@Inject
	@Qualifier(UserDetailsValidator.BEAN_NAME)
	private IValidator userDetailsValidator;

	private static final String DEFAULT_NEW_USER_ROLE_NAME = RolesEnum.USER.getRoleName();

	/**
	 * Validates and then created a {@link User} with the password encoded. Also,
	 * assigns the default role to the user.
	 * 
	 * @param user {@link User} details.
	 * @throws TechnicalException
	 * @throws ValidationException
	 */
	// TODO: make transactional
	public void createUser(User user) throws TechnicalException, ValidationException {

		userDetailsValidator.validate(user);

		encodePassword(user, passwordEncoder);
		assignDefaultRoleToNewUser(user);

		userRepository.save(user);
	}

	/**
	 * Validates and then updates the {@link User} with the details given.
	 * 
	 * @param user    details of user to be updated with.
	 * @param emailId to identify the user whose details needs to be updated.
	 * @throws ValidationException
	 * @throws TechnicalException
	 */
	public void updateUser(User user, String emailId) throws ValidationException, TechnicalException {

		if (Objects.nonNull(user.getEmailId()) || Objects.nonNull(user.getPassword())) {
			LOGGER.error("Sensitive information cannot be updated via the update service.");
			throw new ValidationException("Sensitive information cannot be updated via the update service.");
		}

		user.setEmailId(emailId); // to identify the user whose details needs to be updated.
		userDetailsValidator.validate(user);

		userEntityService.mergeUser(user);
	}

	/**
	 * Gets the {@link User} identified by the given email id.
	 * 
	 * @param emailId id of the {@link User} to be read.
	 * @return {@link User} instance identified by the given email id.
	 * @throws TechnicalException
	 */
	public User getUser(String emailId) throws TechnicalException {

		return userEntityService.getUserByEmailId(emailId);
	}

	/**
	 * Saves the given image of the {@link User}.
	 * 
	 * @param emailId   id of {@link User} whose image needs to be saved.
	 * @param imageFile image file of the {@link User}.
	 * @throws TechnicalException
	 */
	public void saveUserImage(String emailId, MultipartFile imageFile) throws TechnicalException {

		User user = userEntityService.getUserByEmailId(emailId);

		Map<String, Object> parameters = new HashMap<>();
		parameters.put(FILE_ID, user.getImageId());
		try {
			String imageId = fileStorageService.uploadFile(imageFile, parameters);
			user.setImageId(imageId);
			userRepository.save(user);
		} catch (FileStorageException e) {
			LOGGER.error("Error occurred while uploading user image file.");
			throw new TechnicalException(e);
		}
	}

	/**
	 * Deletes image file corresponding to the {@link User} from the storage.
	 * 
	 * @param emailId email id of {@link User} whose image needs to be deleted.
	 * @throws TechnicalException
	 */
	public void deleteUserImage(String emailId) throws TechnicalException {

		User user = userEntityService.getUserByEmailId(emailId);

		if (Objects.isNull(user.getImageId())) {
			LOGGER.error("No image exists to delete for the given user.");
			throw new TechnicalException("No image exists to delete for the given user.");
		}

		Map<String, Object> parameters = new HashMap<>();
		parameters.put(FILE_ID, user.getImageId());
		try {
			fileStorageService.deleteFile(parameters);
			user.setImageId(null);
			userRepository.save(user);
		} catch (FileStorageException e) {
			LOGGER.error("Error occurred while deleting user image file.");
			throw new TechnicalException(e);
		}
	}

	/**
	 * Encodes the password in the given {@link User} instance.
	 * 
	 * @param user            {@link User} instance.
	 * @param passwordEncoder {@link PasswordEncoder} instance.
	 */
	private void encodePassword(User user, PasswordEncoder passwordEncoder) {

		String password = user.getPassword();
		if (!Objects.isNull(password)) {
			String encodedPassword = passwordEncoder.encode(password);
			user.setPassword(encodedPassword);
		}
	}

	/**
	 * Assigns a default role to the given new {@link User}.
	 * 
	 * @param user {@link User} to whom role needs to be assigned.
	 * @throws TechnicalException
	 */
	public void assignDefaultRoleToNewUser(User user) throws TechnicalException {

		Optional<Role> queryResult = roleRepository.findByName(DEFAULT_NEW_USER_ROLE_NAME);
		queryResult.orElseThrow(() -> new TechnicalException("Given role does not exist to be assigned to a user"));
		Role role = queryResult.get();

		UserRoles userRoles = new UserRoles(user, role);
		user.setUserRoles(Arrays.asList(userRoles));
	}
}