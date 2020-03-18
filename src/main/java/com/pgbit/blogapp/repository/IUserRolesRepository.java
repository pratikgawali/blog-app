package com.pgbit.blogapp.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pgbit.blogapp.model.User;
import com.pgbit.blogapp.model.UserRoles;

/**
 * Repository interface for database operations on {@link UserRoles} entity.
 * 
 * @author Pratik Gawali
 *
 */
@Repository
public interface IUserRolesRepository extends JpaRepository<UserRoles, UUID> {

	List<UserRoles> findByUser(User user);
}
