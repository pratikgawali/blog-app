package com.pgbit.blogapp.model;

import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

/**
 * Model class for {@link User} entity.
 * 
 * @author Pratik Gawali
 *
 */
@Entity
@Table(name = "USER")
public class User {

	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Type(type = "uuid-char")
	@Column(name = "ID")
	private UUID id;
	
	@Column(name = "FIRST_NAME", nullable = false)
	private String firstName;
	
	@Column(name = "LAST_NAME")
	private String lastName;
	
	@Column(name = "EMAIL_ID", nullable = false, unique = true)
	private String emailId;
	
	@Column(name = "CREDENTIALS")
	private String credentials;
	
	@Column(name = "IMAGE_ID")
	private String imageId;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@Column(name = "PASSWORD", nullable = false)
	private String password;
	
	@JsonIgnore
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<UserRoles> userRoles;
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getCredentials() {
		return credentials;
	}

	public void setCredentials(String credentials) {
		this.credentials = credentials;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<UserRoles> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(List<UserRoles> userRoles) {
		this.userRoles = userRoles;
	}
}
