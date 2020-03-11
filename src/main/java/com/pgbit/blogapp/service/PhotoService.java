package com.pgbit.blogapp.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PhotoService {
	
	//TODO: make it generic to adhere to any platform file system

	private static final String PHOTO_DIRECTORY_PATH = "\\resources\\photo";

	public boolean saveUserPhoto(MultipartFile photoFile) {

		String baseDirectoryPath = getAbsoluteBaseDirectoryPath();
		if (!createDirectory(baseDirectoryPath)) {
			return false;
		}

		String photoFileName = StringUtils.cleanPath(photoFile.getOriginalFilename());
		Path path = Paths.get(baseDirectoryPath.concat("\\").concat(photoFileName));
		return copyFile(photoFile, path);
	}

	private String getAbsoluteBaseDirectoryPath() {
		return System.getProperty("user.dir").concat(PHOTO_DIRECTORY_PATH);
	}

	private boolean createDirectory(String directoryPath) {
		try {
			Files.createDirectories(Paths.get(directoryPath));
		} catch (IOException e) {
			// TODO add logger
			System.err.println("Error occurred while creating photo directory.");
			return false;
		}
		return true;
	}
	
	private boolean copyFile(MultipartFile fileToCopy, Path toPath) {
		try {
			Files.copy(fileToCopy.getInputStream(), toPath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			// TODO add logger
			System.err.println("Error occurred while saving user photo file.");
			return false;
		}
		return true;
	}
}
