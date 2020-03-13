package com.pgbit.blogapp.service.storage.cloudinary;

import static com.pgbit.blogapp.service.storage.FileStorageParameterKeys.USER_ID;
import static com.pgbit.blogapp.service.storage.FileStorageParameterKeys.USER_IMAGE_FOLDER_PATH;
import static com.pgbit.blogapp.service.storage.cloudinary.CloudinaryKeys.FOLDER;
import static com.pgbit.blogapp.service.storage.cloudinary.CloudinaryKeys.PUBLIC_ID;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.pgbit.blogapp.exception.FileStorageException;
import com.pgbit.blogapp.model.User;
import com.pgbit.blogapp.service.UserService;
import com.pgbit.blogapp.service.storage.IFileStorageService;

/**
 * Service class to upload user image to Cloudinary cloud storage. Also,
 * associates the unique image id generated to the {@link User}.
 * 
 * @author Pratik Gawali
 *
 */
@Service
public class CloudinaryUserImageStorageService implements IFileStorageService {

	private static final String SEPARATOR = "/";

	@Inject
	private Environment env;

	@Inject
	private UserService userService;

	@Inject
	private Cloudinary cloudinary;

	/**
	 * Uploads the given image file to the Cloudinary cloud storage. If a user's
	 * image already exists, it will be overridden by the given new file.
	 * 
	 * @param imageFile  user image file to be uploaded.
	 * @param parameters contains the user id.
	 * @throws FileStorageException
	 */
	@Override
	public void uploadFile(MultipartFile imageFile, Map<String, Object> parameters) throws FileStorageException {

		// fetch user details
		String userId = String.valueOf(parameters.get(USER_ID));
		User user = userService.getUser(UUID.fromString(userId));
		if (!userExists(user)) {
			// TODO: add logger message
			throw new FileStorageException("User does not exist for whom the image is to be uploaded.");
		}

		// delete previous user image
		deleteImage(user.getImageId());

		// upload new image and save its image id
		String qualifiedImageId = uploadImage(imageFile, user.getImageId());
		String imageId = fetchImageId(qualifiedImageId);
		user.setImageId(imageId);
		userService.saveUser(user);
	}

	/**
	 * Deletes image (if exists) from Cloudinary cloud storage.
	 * 
	 * @param imageId id of the image to be deleted.
	 * @throws FileStorageException
	 */
	private void deleteImage(String imageId) throws FileStorageException {

		if (imageExists(imageId)) {
			try {
				String qualifiedImageId = prepareQualifiedImageId(imageId);
				cloudinary.uploader().destroy(qualifiedImageId, ObjectUtils.emptyMap());
			} catch (IOException e) {
				// TODO add logger image
				throw new FileStorageException(e);
			}
		}
	}

	/**
	 * Uploads image file to the Cloudinary cloud storage.
	 * 
	 * @param imageFile the user image file to be uploaded.
	 * @param imageId   id of the image if exists, otherwise null.
	 * @return qualified image id of the uploaded image.
	 * @throws FileStorageException
	 */
	private String uploadImage(MultipartFile imageFile, String imageId) throws FileStorageException {

		try {
			Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(),
					ObjectUtils.asMap(FOLDER, env.getProperty(USER_IMAGE_FOLDER_PATH), PUBLIC_ID, imageId));

			return String.valueOf(uploadResult.get(PUBLIC_ID));

		} catch (IOException e) {
			// TODO add logger message
			throw new FileStorageException(e);
		}
	}

	/**
	 * Fetched image id from the given qualified image id i.e. from the full path.
	 * 
	 * @param qualifiedImageId the full path of the image.
	 * @return image id extracted from the given qualified image id.
	 */
	private String fetchImageId(String qualifiedImageId) {

		String[] pathComponents = qualifiedImageId.split(SEPARATOR);
		int size = pathComponents.length;
		return size > 1 ? pathComponents[size - 1] : qualifiedImageId;
	}

	/**
	 * Gets qualified image id i.e. full path of the given image id.
	 * 
	 * @param imageId
	 * @return full path of the given image id.
	 */
	private String prepareQualifiedImageId(String imageId) {
		return Objects.isNull(imageId) ? env.getProperty(USER_IMAGE_FOLDER_PATH)
				: env.getProperty(USER_IMAGE_FOLDER_PATH, "").concat(imageId);
	}

	/**
	 * Checks if the image with given image id exists.
	 * 
	 * @param imageId the id of the image
	 * @return true if image exits already, otherwise false
	 */
	private boolean imageExists(String imageId) {
		return !Objects.isNull(imageId);
	}

	/**
	 * Checks if the given user exists.
	 * 
	 * @param user instance of the {@link User}.
	 * @return true if user exists, otherwise false.
	 */
	private boolean userExists(User user) {
		return !Objects.isNull(user);
	}
}
