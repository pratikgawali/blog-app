package com.pgbit.blogapp.authentication.jwt.filter;

import java.io.IOException;
import java.util.Objects;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.pgbit.blogapp.authentication.jwt.util.JwtUtil;
import com.pgbit.blogapp.authentication.service.CustomUserDetailsService;

/**
 * Filter class for enabling JWT authorization for all incoming request for
 * protected APIs. The filter is added before
 * {@link UsernamePasswordAuthenticationFilter}.
 * 
 * @author Pratik Gawali
 *
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Inject
	private CustomUserDetailsService userDetailsService;

	@Inject
	private JwtUtil jwtUtil;

	private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
	private static final String JWT_PREFIX = "Bearer ";

	/**
	 * Extracts JWT token from the incoming request header, verifies it and stores
	 * authentication information into the security context.
	 * 
	 * @param request     incoming request
	 * @param response
	 * @param filterChain the chain of servlet filters
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER_KEY);

		String userName = null;
		String jwt = null;

		// TODO make constants
		if (!Objects.isNull(authorizationHeader) && authorizationHeader.startsWith(JWT_PREFIX)) {
			jwt = authorizationHeader.substring(JWT_PREFIX.length());
			userName = jwtUtil.extractUsername(jwt);
		}

		if (!Objects.isNull(userName) && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
			if (jwtUtil.validateToken(jwt, userDetails)) {

				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}

		filterChain.doFilter(request, response);
	}

}
