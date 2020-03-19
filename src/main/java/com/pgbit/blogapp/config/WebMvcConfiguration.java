package com.pgbit.blogapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for Web MVC.
 * 
 * @author Pratik Gawali
 *
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

	@Value("${cors.request.mapping}")
	private String CORS_REQUEST_MAPPING;

	@Value("${cors.allowed.origins}")
	private String CORS_ALLOWED_ORIGIN;

	/**
	 * Registers CORS mapping.
	 *
	 * @param registry
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping(CORS_REQUEST_MAPPING).allowedOrigins(CORS_ALLOWED_ORIGIN);
	}
}
