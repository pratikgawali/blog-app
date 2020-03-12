package com.pgbit.blogapp.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;

public class FileUtils {
	
	private static final String USER_DIRECTORY_PROPERTY = "user.dir";
	private static final String SEPARATOR = "/";

	public static String getAbsoluteBaseDirectoryPath(String directoryPathName) {
		return System.getProperty(USER_DIRECTORY_PROPERTY).concat(directoryPathName);
	}

	public static String getAbsoluteFilePath(String directoryPathName, String fileName) {
		return getAbsoluteBaseDirectoryPath(directoryPathName).concat(SEPARATOR).concat(fileName);
	}

	public static boolean createDirectory(String directoryPath) {
		try {
			Files.createDirectories(Paths.get(directoryPath));
		} catch (IOException e) {
			// TODO add logger
			System.err.println("Error occurred while creating directory.");
			return false;
		}
		return true;
	}

	public static boolean copyFile(MultipartFile fileToCopy, Path toPath) {
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
