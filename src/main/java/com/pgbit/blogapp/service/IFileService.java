package com.pgbit.blogapp.service;

import org.springframework.web.multipart.MultipartFile;

public interface IFileService {
	
	boolean uploadFile(MultipartFile file, String directoryPathName, String fileName);
}
