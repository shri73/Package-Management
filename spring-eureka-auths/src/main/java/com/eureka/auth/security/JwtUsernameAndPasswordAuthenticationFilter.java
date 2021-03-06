package com.eureka.auth.security;

import java.io.IOException;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

import com.eureka.common.security.JwtConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter   {
	
	// using auth manager to validate the user credentials
	private AuthenticationManager authManager;
	
	private final JwtConfig jwtConfig;
    
	public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authManager, JwtConfig jwtConfig) {
		this.authManager = authManager;
		this.jwtConfig = jwtConfig;
		
		
		this.setRequiresAuthenticationRequestMatcher(new OrRequestMatcher(new AntPathRequestMatcher(jwtConfig.getUri(), "POST"),
				new AntPathRequestMatcher("/admin/login", "POST")));
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
        try {
			
			// 1. Get credentials from request
			UserCredentials creds = new ObjectMapper().readValue(request.getInputStream(), UserCredentials.class);
			
			// 2. Create auth object (contains credentials) which will be used by auth manager
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
					creds.getUsername(), creds.getPassword(), Collections.emptyList());
			
			// 3. Authentication manager authenticate the user, and use UserDetialsServiceImpl::loadUserByUsername() method to load the user.
			return authManager.authenticate(authToken);
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	 

		/**
		 * Provided so that subclasses may configure what is put into the authentication
		 * request's details property.
		 *
		 * @param request that an authentication request is being created for
		 * @param authRequest the authentication request object that should have its details
		 * set
		 */
		protected void setDetails(HttpServletRequest request,
				UsernamePasswordAuthenticationToken authRequest) {
			authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
		}

	
	// Upon successful authentication, generate a token.
	// The 'auth' passed to successfulAuthentication() is the current authenticated user.
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication auth) throws IOException, ServletException {
		Long expirationTimeLong = Integer.toUnsignedLong(jwtConfig.getExpiration()); //in second
		
		final Date createdDate = new Date();
		final Date expirationDate = new Date(createdDate.getTime() + expirationTimeLong * 1000);

		
		Key keysKey = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes());
		String token = Jwts.builder()
			.setSubject(auth.getName())	
			.claim("authorities", auth.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
			.setIssuedAt(createdDate)
			.setExpiration(expirationDate)
			.signWith(keysKey)
			.compact();
		
		// Add token to header
		response.addHeader(jwtConfig.getHeader(), jwtConfig.getPrefix() + token);
	}
	
	// A (temporary) class just to represent the user credentials
	private static class UserCredentials {
	    private String username, password;
	    
	    public String getUsername() {
			return username;
		}
	    
	    public void setUsername(String username) {
			this.username = username;
		}
	    public String getPassword() {
			return password;
		}
	    public void setPassword(String password) {
			this.password = password;
		}
	}
}
