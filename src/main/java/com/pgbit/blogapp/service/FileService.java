package com.pgbit.blogapp.service;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService implements IFileService {

	@Override
	public boolean uploadFile(MultipartFile file, String directoryPathName, String fileName) {

		String baseDirectoryPath = FileUtils.getAbsoluteBaseDirectoryPath(directoryPathName);
		if (!FileUtils.createDirectory(baseDirectoryPath)) {
			return false;
		}

		Path path = Paths.get(FileUtils.getAbsoluteFilePath(directoryPathName, fileName));
		return FileUtils.copyFile(file, path);
	}
}
