package com.pgbit.blogapp.controller;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pgbit.blogapp.exception.TechnicalException;
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
	 * @param post {@link Post} instance to be saved.
	 * @return the saved {@link Post} instance.
	 */
	@PostMapping
	public Post savePost(@RequestBody Post post) {
		return postService.savePost(post);
	}

	/**
	 * Delete a {@link Post} identified by the given post id.
	 * 
	 * @param postId id of the {@link Post} to be deleted.
	 */
	@DeleteMapping("/{postId}")
	public void deletePost(@PathVariable(name = "postId") UUID postId) {
		postService.deletePost(postId);
	}

	/**
	 * Upvote a {@link Post} identified by the given post id.
	 * 
	 * @param postId id of the {@link Post} that needs to be upvoted.
	 * @throws TechnicalException
	 */
	@PostMapping("/upVote/{postId}")
	public void incrementUpVotes(@PathVariable(name = "postId") UUID postId) throws TechnicalException {
		postService.incrementUpVotes(postId);
	}
}
