package com.pgbit.blogapp.service;

import java.util.Objects;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.pgbit.blogapp.exception.TechnicalException;
import com.pgbit.blogapp.model.User;
import com.pgbit.blogapp.repository.IUserRepository;

/**
 * Service class for {@link User} entity operations.
 * 
 * @author Pratik Gawali
 *
 */
@Service
public class UserEntityService {

	@Inject
	IUserRepository userRepository;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserEntityService.class);

	/**
	 * Identifies the user as new if the given email id does not exist in the
	 * database.
	 * 
	 * @param emailId
	 * @return true if {@link User} with given email id does not exist already,
	 *         otherwise false.
	 */
	public boolean isNewUser(String emailId) {

		Optional<User> queryResult = userRepository.findByEmailId(emailId);
		return !queryResult.isPresent();
	}

	/**
	 * Gets {@link User} by email id.
	 * 
	 * @param emailId
	 * @return {@link User} instance having given email id.
	 * @throws TechnicalException
	 */
	public User getUserByEmailId(String emailId) throws TechnicalException {

		Optional<User> queryResult = userRepository.findByEmailId(emailId);
		queryResult.orElseThrow(() -> {
			LOGGER.error("No user exists with the given email id.");
			return new TechnicalException("No user exists with the given email id.");
		});

		return queryResult.get();
	}

	/**
	 * Merges {@link User} by updating fields in the saved instance.
	 * 
	 * @param updatedUser updated {@link User} instance.
	 * @throws TechnicalException
	 */
	public void mergeUser(User updatedUser) throws TechnicalException {

		Optional<User> queryResult = userRepository.findByEmailId(updatedUser.getEmailId());
		queryResult.orElseThrow(
				() -> new TechnicalException("User does not exist whose information needs to be updated."));

		User existingUser = queryResult.get();

		updatedUser.setId(existingUser.getId());

		if (Objects.nonNull(updatedUser.getFirstName())) {
			existingUser.setFirstName(updatedUser.getFirstName());
		}

		if (Objects.nonNull(updatedUser.getLastName())) {
			existingUser.setLastName(updatedUser.getLastName());
		}

		if (Objects.nonNull(updatedUser.getCredentials())) {
			existingUser.setCredentials(updatedUser.getCredentials());
		}

		if (Objects.nonNull(updatedUser.getImageId())) {
			existingUser.setImageId(updatedUser.getImageId());
		}

		if (Objects.nonNull(updatedUser.getPassword())) {
			existingUser.setPassword(updatedUser.getPassword());
		}

		userRepository.save(existingUser);
	}
}
