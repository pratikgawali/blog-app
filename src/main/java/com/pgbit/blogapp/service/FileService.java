package com.pgbit.blogapp.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {
	
	//TODO: make it generic to adhere to any platform file system
	
	private String directoryPathName;
	
	public void setDirectoryPathName(String directoryPathName) {
		this.directoryPathName = directoryPathName;
	}

	public boolean saveFile(MultipartFile file, String fileName) {

		String baseDirectoryPath = getAbsoluteBaseDirectoryPath();
		if (!createDirectory(baseDirectoryPath)) {
			return false;
		}

		Path path = Paths.get(baseDirectoryPath.concat("\\").concat(fileName));
		return copyFile(file, path);
	}

	private String getAbsoluteBaseDirectoryPath() {
		return System.getProperty("user.dir").concat(directoryPathName);
	}

	private boolean createDirectory(String directoryPath) {
		try {
			Files.createDirectories(Paths.get(directoryPath));
		} catch (IOException e) {
			// TODO add logger
			System.err.println("Error occurred while creating directory.");
			return false;
		}
		return true;
	}
	
	private boolean copyFile(MultipartFile fileToCopy, Path toPath) {
		try {
			Files.copy(fileToCopy.getInputStream(), toPath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			// TODO add logger
			System.err.println("Error occurred while saving file.");
			return false;
		}
		return true;
	}
}
