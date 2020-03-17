package com.pgbit.blogapp.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "USER_ROLES", uniqueConstraints = @UniqueConstraint(columnNames = { "USER_ID", "ROLE_ID" }))
public class UserRoles {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Type(type = "uuid-char")
	@Column(name = "ID")
	private UUID id;

	@ManyToOne
	@JoinColumn(name = "USER_ID", nullable = false)
	private User user;

	@ManyToOne
	@JoinColumn(name = "ROLE_ID", nullable = false)
	private Role role;

	public UserRoles() {
	}

	public UserRoles(User user, Role role) {
		this.user = user;
		this.role = role;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
}
