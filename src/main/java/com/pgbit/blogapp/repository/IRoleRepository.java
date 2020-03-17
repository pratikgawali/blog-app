package com.pgbit.blogapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pgbit.blogapp.model.Role;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Integer>{
	
	Optional<Role> findByName(String roleName);
}
