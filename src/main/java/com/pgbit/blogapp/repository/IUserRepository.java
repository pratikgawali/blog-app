package com.pgbit.blogapp.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pgbit.blogapp.model.User;

/**
 * Repository interface for database operations on {@link User} entity.
 * 
 * @author Pratik Gawali
 *
 */
@Repository
public interface IUserRepository extends JpaRepository<User, UUID> {
	
	Optional<User> findByEmailId(String emailId);
}