package com.pgbit.blogapp.service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.pgbit.blogapp.exception.TechnicalException;
import com.pgbit.blogapp.model.Post;
import com.pgbit.blogapp.repository.IPostRepository;

@Service
public class PostService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PostService.class);

	@Inject
	private IPostRepository repository;

	public Post savePost(Post post) {
		return repository.save(post);
	}

	public Post readPost(UUID postId) throws TechnicalException {

		Post post = repository.findById(postId).orElse(null);
		
		if (Objects.isNull(post)) {
			LOGGER.error("Post id given to read does not exist.");
			throw new TechnicalException("Post id given to read does not exist.");
		}
		
		Long views = post.getViews();
		post.setViews(views + 1);
		repository.save(post);
		return post;
	}

	public List<Post> readPosts() {
		return repository.findAll();
	}

	public void deletePost(UUID postId) {
		repository.deleteById(postId);
	}

	public void incrementUpVotes(UUID postId) throws TechnicalException {

		Post post = repository.findById(postId).orElse(null);
		if (Objects.isNull(post)) {
			LOGGER.error("Post id given for up vote does not exist.");
			throw new TechnicalException("Post id given for up vote does not exist.");
		}

		Long upVotes = post.getUpVotes();
		post.setUpVotes(upVotes + 1);
		repository.save(post);
	}
}