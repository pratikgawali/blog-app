package com.pgbit.blogapp.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pgbit.blogapp.model.User;

@Repository
public interface IUserRepository extends JpaRepository<User, UUID> {

}
