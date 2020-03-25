package com.pgbit.blogapp.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Model class for {@link Post} entity.
 * 
 * @author Pratik Gawali
 *
 */
@Entity
@Table(name = "POST")
@EntityListeners(AuditingEntityListener.class)
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Type(type = "uuid-char")
	@Column(name = "ID")
	private UUID id;

	@Column(name = "TITLE", nullable = false)
	private String title;

	@Lob
	@Column(name = "CONTENT", nullable = false)
	private String content;

	@Column(name = "VIEWS", nullable = false)
	private Long views;

	@Column(name = "UP_VOTES", nullable = false)
	private Long upVotes;

	@CreatedDate
	@Column(name = "CREATED_AT")
	private Date createdAt;

	@LastModifiedDate
	@Column(name = "LAST_MODIFIED_AT")
	private Date lastModifiedAt;

	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "AUTHOR", nullable = false)
	private User author;
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getViews() {
		return views;
	}

	public void setViews(Long views) {
		this.views = views;
	}

	public Long getUpVotes() {
		return upVotes;
	}

	public void setUpVotes(Long upVotes) {
		this.upVotes = upVotes;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getLastModifiedAt() {
		return lastModifiedAt;
	}

	public void setLastModifiedAt(Date lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}
}
