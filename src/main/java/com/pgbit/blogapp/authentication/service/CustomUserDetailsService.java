package com.pgbit.blogapp.authentication.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.inject.Inject;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pgbit.blogapp.authentication.jwt.model.CustomUserDetails;
import com.pgbit.blogapp.model.User;
import com.pgbit.blogapp.model.UserRoles;
import com.pgbit.blogapp.repository.IUserRepository;
import com.pgbit.blogapp.repository.IUserRolesRepository;

/**
 * Implementation of {@link UserDetailsService} to be used by Spring Security.
 * 
 * @author Pratik Gawali
 *
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

	// authority starting with ROLE_ is identified as a role
	private static final String ROLE_PREFIX = "ROLE_";

	@Inject
	private IUserRepository userRepository;

	@Inject
	private IUserRolesRepository userRolesRepository;

	/**
	 * Loads user info fetched by username (emailId) and his authorities into
	 * {@link UserDetails}.
	 *
	 * @param username email id of the user
	 * @return user info along with his authorities as an instance of
	 *         {@link UserDetails}.
	 *
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<User> userQueryResult = userRepository.findByEmailId(username);
		userQueryResult
				.orElseThrow(() -> new UsernameNotFoundException("User does not exists with the given email id."));
		User user = userQueryResult.get();

		List<UserRoles> userRoles = userRolesRepository.findByUser(user);
		if (Objects.isNull(userRoles) || userRoles.isEmpty()) {
			throw new UsernameNotFoundException("No role found for the given user.");
		}

		List<GrantedAuthority> authorities = new ArrayList<>();
		for (UserRoles userRole : userRoles) {
			authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX.concat(userRole.getRole().getName())));
		}

		return new CustomUserDetails(user.getEmailId(), user.getPassword(), authorities);
	}

}
