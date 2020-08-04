package com.eureka.auth.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.eureka.auth.model.Admin;
import com.eureka.auth.model.Customer;
import com.eureka.auth.model.Role;


public class UserServiceDetails extends User {
	
	public UserServiceDetails(String username, String password, Role role) {
		super(username, password, authorities(role));
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 1L;
	
	
	
	private static Collection<? extends GrantedAuthority> authorities(Role role) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (role.getRole() == "ADMIN") {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        return authorities;
    }

  
	private UserDetails toUserDetails(UserObject userObject) {
        return User.withUsername(userObject.name)
                   .password(userObject.password)
                   .roles(userObject.role).build();
    }
	
    private static class UserObject {
        private String name;
        private String password;
        private String role;

        public UserObject(String name, String password, String role) {
            this.name = name;
            this.password = password;
            this.role = role;
        }
    }
	
	
}
