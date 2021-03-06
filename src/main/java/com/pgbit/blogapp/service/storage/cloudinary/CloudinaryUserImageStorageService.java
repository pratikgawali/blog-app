package com.pgbit.blogapp.service.storage.cloudinary;

import static com.pgbit.blogapp.service.storage.FileStorageParameterKeys.FILE_ID;
import static com.pgbit.blogapp.service.storage.FileStorageParameterKeys.USER_IMAGE_FOLDER_PATH;
import static com.pgbit.blogapp.service.storage.cloudinary.CloudinaryKeys.FOLDER;
import static com.pgbit.blogapp.service.storage.cloudinary.CloudinaryKeys.OK;
import static com.pgbit.blogapp.service.storage.cloudinary.CloudinaryKeys.PUBLIC_ID;
import static com.pgbit.blogapp.service.storage.cloudinary.CloudinaryKeys.RESULT;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.pgbit.blogapp.exception.FileStorageException;
import com.pgbit.blogapp.model.User;
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

	private static final Logger LOGGER = LoggerFactory.getLogger(CloudinaryUserImageStorageService.class);

	@Inject
	private Environment env;

	@Inject
	private Cloudinary cloudinary;

	/**
	 * Uploads the given image file to the Cloudinary cloud storage. If a user's
	 * image already exists, it will be overridden by the given new file.
	 * 
	 * @param imageFile  user image file to be uploaded.
	 * @param parameters contains the user id.
	 * @return
	 * @throws FileStorageException
	 */
	@Override
	public String uploadFile(MultipartFile imageFile, Map<String, Object> parameters) throws FileStorageException {

		String imageId = Objects.isNull(parameters.get(FILE_ID)) ? null : String.valueOf(parameters.get(FILE_ID));

		String qualifiedImageId = uploadImage(imageFile, imageId);
		return fetchImageId(qualifiedImageId);
	}

	/**
	 * Deletes the image file associated to the given user.
	 * 
	 * @param parameters contains userId to identify user.
	 * @throws FileStorageException
	 */
	@Override
	public void deleteFile(Map<String, Object> parameters) throws FileStorageException {

		String imageId = String.valueOf(parameters.get(FILE_ID));
		try {
			String qualifiedImageId = prepareQualifiedImageId(imageId);
			Map deleteResponse = cloudinary.uploader().destroy(qualifiedImageId, ObjectUtils.emptyMap());

			if (!isCloudinaryDeleteOperationSuccess(deleteResponse)) {
				LOGGER.error("Unsuccessful in deleting user image file from Cloudinary cloud storage");
				throw new FileStorageException(
						"Unsuccessful in deleting user image file from Cloudinary cloud storage");
			}
		} catch (IOException e) {
			LOGGER.error("Exception occurred while deleting user image file from Cloudinary cloud storage");
			throw new FileStorageException(e);
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
			LOGGER.error("Exception occurred while uploading user image file to Cloudinary cloud storage");
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
	 * Check if the delete operation performed by Cloudinary is successful.
	 * 
	 * @param deleteResponse the response of delete operation returned from
	 *                       Cloudinary API.
	 * @return true if delete was successful, otherwise false.
	 */
	private boolean isCloudinaryDeleteOperationSuccess(Map deleteResponse) {
		return OK.equals(deleteResponse.get(RESULT));
	}
}
