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

/**
 * Service class for operations on {@link Post} entity.
 * 
 * @author Pratik Gawali
 *
 */
@Service
public class PostService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PostService.class);

	@Inject
	private IPostRepository repository;

	/**
	 * Saves the given {@link Post}.
	 * 
	 * @param post {@link Post} instance to be saved.
	 * @return the {@link Post} saved instance.
	 */
	public Post savePost(Post post) {
		return repository.save(post);
	}

	/**
	 * Gets {@link Post} and increment its views count.
	 * 
	 * @param postId id of the {@link Post} to be read.
	 * @return the {@link Post} identified by the given post id.
	 * @throws TechnicalException
	 */
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

	/**
	 * Gets all saved {@link Post}.
	 * 
	 * @return list of all saved {@link Post}.
	 */
	public List<Post> readPosts() {
		return repository.findAll();
	}

	/**
	 * Deletes the {@link Post} identified by the given post id.
	 * 
	 * @param postId id of the {@link Post} to be deleted.
	 */
	public void deletePost(UUID postId) {
		repository.deleteById(postId);
	}

	/**
	 * Increments the up vote count for the {@link Post} identified by the given
	 * post id.
	 * 
	 * @param postId id of the {@link Post} whose up vote count needs to be
	 *               incremented.
	 * @throws TechnicalException
	 */
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