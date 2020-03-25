package com.pgbit.blogapp.controller;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pgbit.blogapp.exception.TechnicalException;
import com.pgbit.blogapp.exception.ValidationException;
import com.pgbit.blogapp.model.Post;
import com.pgbit.blogapp.service.PostService;

/**
 * Controller class for handling requests related to {@link Post}.
 * 
 * @author Pratik Gawali
 *
 */
@RestController
@RequestMapping("/api/posts")
public class PostController {

	@Inject
	private PostService postService;

	/**
	 * Gets a list of {@link Post}.
	 * 
	 * @return a list of {@link Post}.
	 */
	@GetMapping
	public List<Post> getPosts() {
		return postService.readPosts();
	}

	/**
	 * Gets a {@link Post} identified by the given post id.
	 * 
	 * @param postId id of the post.
	 * @return {@link Post} with the given postId as its primary key value.
	 * @throws TechnicalException
	 */
	@GetMapping("/{postId}")
	public Post getPost(@PathVariable(name = "postId") UUID postId) throws TechnicalException {
		return postService.readPost(postId);
	}

	/**
	 * Saves a new {@link Post} to the database.
	 * 
	 * @param principal identifies currently logged in user.
	 * @param post      {@link Post} instance to be saved.
	 * @throws TechnicalException
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	@PostMapping
	public void createPost(Principal principal, @RequestBody Post post) throws TechnicalException {

		String emailId = principal.getName();
		postService.createPost(post, emailId);
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@PostMapping("/update")
	public void updatePost(Principal principal, @RequestBody Post post) throws TechnicalException, ValidationException {

		String emailId = principal.getName();
		postService.updatePost(post, emailId);
	}

	/**
	 * Delete a {@link Post} identified by the given post id.
	 * 
	 * @param principal identifies currently logged in user.
	 * @param postId    id of the {@link Post} to be deleted.
	 * @throws ValidationException
	 * @throws TechnicalException
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	@DeleteMapping("/{postId}")
	public void deletePost(Principal principal, @PathVariable(name = "postId") UUID postId)
			throws TechnicalException, ValidationException {

		String emailId = principal.getName();
		postService.deletePost(postId, emailId);
	}

	/**
	 * Upvote a {@link Post} identified by the given post id.
	 * 
	 * @param principal identifies currently logged in user.
	 * @param postId    id of the {@link Post} that needs to be upvoted.
	 * @throws TechnicalException
	 * @throws ValidationException
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	@PostMapping("/upVote/{postId}")
	public void incrementUpVotes(Principal principal, @PathVariable(name = "postId") UUID postId)
			throws TechnicalException, ValidationException {

		String emailId = principal.getName();
		postService.incrementUpVotes(postId, emailId);
	}
}
