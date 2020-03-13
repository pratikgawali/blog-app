package com.pgbit.blogapp.service.storage.cloudinary;

import static com.pgbit.blogapp.service.storage.cloudinary.CloudinaryKeys.API_KEY;
import static com.pgbit.blogapp.service.storage.cloudinary.CloudinaryKeys.API_SECRET;
import static com.pgbit.blogapp.service.storage.cloudinary.CloudinaryKeys.CLOUD_NAME;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Configuration
public class CloudinaryConfig {
	
	@Inject
	private Environment env;

	@Bean
	public Cloudinary cloudinary() {
		return new Cloudinary(ObjectUtils.asMap(
				CLOUD_NAME, env.getProperty(CLOUD_NAME), 
				API_KEY, env.getProperty(API_KEY), 
				API_SECRET, env.getProperty(API_SECRET)));
	}
}
