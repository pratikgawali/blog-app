package com.pgbit.blogapp.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.pgbit.blogapp.exception.TechnicalException;
import com.pgbit.blogapp.exception.ValidationException;
import com.pgbit.blogapp.model.Post;
import com.pgbit.blogapp.model.User;
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
	private IPostRepository postRepository;

	@Inject
	private UserEntityService userEntityService;

	/**
	 * Creates a new {@link Post}.
	 * 
	 * @param post    {@link Post} details.
	 * @param emailId email id of the author of the post.
	 * @throws TechnicalException
	 */
	public void createPost(Post post, String emailId) throws TechnicalException {

		User author = userEntityService.getUserByEmailId(emailId);
		post.setAuthor(author);

		// new post
		if (Objects.isNull(post.getId())) {
			post.setUpVotes(0L);
			post.setViews(0L);
		}

		postRepository.save(post);
	}

	/**
	 * Updates the already existing {@link Post}.
	 * 
	 * @param post    contains updated {@link Post} contents.
	 * @param emailId email id of the user who is initiating the update of the post.
	 * @throws TechnicalException
	 * @throws ValidationException
	 */
	public void updatePost(Post post, String emailId) throws TechnicalException, ValidationException {

		Optional<Post> queryResult = postRepository.findById(post.getId());
		queryResult.orElseThrow(() -> {
			LOGGER.error("No post exists with the given post id to update.");
			return new TechnicalException("No post exists with the given post id to update.");
		});

		Post existingPost = queryResult.get();

		boolean isAuthorOfPost = existingPost.getAuthor().getEmailId().equals(emailId);
		if (!isAuthorOfPost) {
			LOGGER.error("Only author of the post is allowed to update his post.");
			throw new ValidationException("Only author of the post is allowed to update his post.");
		}

		// only content and title of the post can be updated
		if (Objects.nonNull(post.getContent())) {
			existingPost.setContent(post.getContent());
		}
		if (Objects.nonNull(post.getTitle())) {
			existingPost.setTitle(post.getTitle());
		}

		postRepository.save(existingPost);
	}

	/**
	 * Gets {@link Post} and increment its views count.
	 * 
	 * @param postId id of the {@link Post} to be read.
	 * @return the {@link Post} identified by the given post id.
	 * @throws TechnicalException
	 */
	public Post readPost(UUID postId) throws TechnicalException {

		Optional<Post> queryResult = postRepository.findById(postId);
		queryResult.orElseThrow(() -> {
			LOGGER.error("Post id given to read does not exist.");
			return new TechnicalException("Post id given to read does not exist.");
		});

		Post post = queryResult.get();

		Long views = post.getViews();
		post.setViews(views + 1);
		postRepository.save(post);
		return post;
	}

	/**
	 * Gets all saved {@link Post}.
	 * 
	 * @return list of all saved {@link Post}.
	 */
	public List<Post> readPosts() {
		return postRepository.findAll();
	}

	/**
	 * Deletes the {@link Post} identified by the given post id.
	 * 
	 * @param postId  id of the {@link Post} to be deleted.
	 * @param emailId email id of user who wants to delete the post.
	 * @throws TechnicalException
	 * @throws ValidationException
	 */
	public void deletePost(UUID postId, String emailId) throws TechnicalException, ValidationException {

		Optional<Post> queryResult = postRepository.findById(postId);
		queryResult.orElseThrow(() -> {
			LOGGER.error("No post exists with the given post id to delete.");
			return new TechnicalException("No post exists with the given post id to delete.");
		});

		Post post = queryResult.get();
		boolean isAuthorOfPost = post.getAuthor().getEmailId().equals(emailId);
		if (!isAuthorOfPost) {
			LOGGER.error("Only author of the post is allowed to delete the post.");
			throw new ValidationException("Only author of the post is allowed to delete the post.");
		}

		postRepository.deleteById(postId);
	}

	/**
	 * Increments the up vote count for the {@link Post} identified by the given
	 * post id.
	 * 
	 * @param postId  id of the {@link Post} whose up vote count needs to be
	 *                incremented.
	 * @param emailId email id of user who wants to up vote the post.
	 * @throws TechnicalException
	 * @throws ValidationException
	 */
	public void incrementUpVotes(UUID postId, String emailId) throws TechnicalException, ValidationException {

		Optional<Post> queryResult = postRepository.findById(postId);
		queryResult.orElseThrow(() -> {
			LOGGER.error("No post exists with the given post id to upvote.");
			return new TechnicalException("No post exists with the given post id to upvote.");
		});

		Post post = queryResult.get();
		boolean isAuthorOfPost = post.getAuthor().getEmailId().equals(emailId);
		if (isAuthorOfPost) {
			LOGGER.error("Author is not allowed to up vote his own post.");
			throw new ValidationException("Author is not allowed to up vote his own post.");
		}

		Long upVotes = post.getUpVotes();
		post.setUpVotes(upVotes + 1);
		postRepository.save(post);
	}
}