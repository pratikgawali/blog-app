package com.pgbit.blogapp.service.storage.cloudinary;

import com.cloudinary.Cloudinary;

/**
 * Constants class for storing key strings relevant for {@link Cloudinary} operations.
 * 
 * @author Pratik Gawali
 *
 */
public class CloudinaryKeys {

	// credentials keys
	static final String CLOUD_NAME = "cloud_name";
	static final String API_KEY = "api_key";
	static final String API_SECRET = "api_secret";

	// request parameter keys
	static final String FOLDER = "folder";
	
	// response parameter keys
	static final String PUBLIC_ID = "public_id";
	static final String RESULT = "result";
	
	// response parameter value
	static final String OK = "ok";
}
