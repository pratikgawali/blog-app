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

@RestController
@RequestMapping("/api/posts")
public class PostController {

	@Inject
	private PostService postService;

	@GetMapping
	public List<Post> getPosts() {
		return postService.readPosts();
	}

	@GetMapping("/{postId}")
	public Post getPost(@PathVariable(name = "postId") UUID postId) throws TechnicalException {
		return postService.readPost(postId);
	}

	@PostMapping
	public Post savePost(@RequestBody Post post) {
		return postService.savePost(post);
	}

	@DeleteMapping("/{postId}")
	public void deletePost(@PathVariable(name = "postId") UUID postId) {
		postService.deletePost(postId);
	}
	
	@PostMapping("/upVote/{postId}")
	public void incrementUpVotes(@PathVariable(name = "postId") UUID postId) throws TechnicalException {
		postService.incrementUpVotes(postId);
	}
}
