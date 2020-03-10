package com.pgbit.blogapp.service;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.pgbit.blogapp.model.Post;
import com.pgbit.blogapp.repository.IPostRepository;

@Service
public class PostService {
	
	@Inject
	private IPostRepository repository;

	public Post savePost(Post post) {
		return repository.save(post);
	}
	
	public Post readPost(UUID id) {
		return repository.findById(id).orElse(null);
	}
	
	public List<Post> readPosts() {
		return repository.findAll();
	}
	
	public void deletePost(UUID id) {
		repository.deleteById(id);
	}
}