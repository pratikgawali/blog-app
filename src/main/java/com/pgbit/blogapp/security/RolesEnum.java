package com.pgbit.blogapp.security;

public enum RolesEnum {

	USER("USER"), ADMIN("ADMIN");
	
	private String roleName;
	
	private RolesEnum(String roleId) {
		this.roleName = roleId;
	}
	
	public String getRoleName() {
		return this.roleName;
	}
}
