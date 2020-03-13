package com.pgbit.blogapp.service.storage;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.pgbit.blogapp.exception.FileStorageException;

public interface IFileStorageService {
	
	void uploadFile(MultipartFile file, Map<String, Object> parameters) throws FileStorageException;
}
