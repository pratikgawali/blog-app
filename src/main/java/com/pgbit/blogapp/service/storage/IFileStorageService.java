package com.pgbit.blogapp.service.storage;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.pgbit.blogapp.exception.FileStorageException;

/**
 * Interface to declare contract for file operations.
 * 
 * @author Pratik Gawali
 *
 */
public interface IFileStorageService {

	/**
	 * Uploads the given multipart file to a storage.
	 * 
	 * @param file       to be uploaded.
	 * @param parameters that may be required for {@link IFileStorageService}
	 *                   implementor.
	 * @throws FileStorageException
	 */
	void uploadFile(MultipartFile file, Map<String, Object> parameters) throws FileStorageException;

	/**
	 * Deletes the file identified by the given parameters from storage.
	 * 
	 * @param parameters that may be required for {@link IFileStorageService}
	 *                   implementor like to identify the file to be deleted.
	 * @throws FileStorageException
	 */
	void deleteFile(Map<String, Object> parameters) throws FileStorageException;
}
