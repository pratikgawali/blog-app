package com.pgbit.blogapp.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "ROLE")
public class Role {

	@Id
	@Column(name = "ID")
	private Integer id;
	
	@Column(name = "NAME", nullable = false, unique = true)
	private String name;
	
	@JsonIgnore
	@OneToMany(mappedBy = "role", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private List<UserRoles> userRoles;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<UserRoles> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(List<UserRoles> userRoles) {
		this.userRoles = userRoles;
	}
}
