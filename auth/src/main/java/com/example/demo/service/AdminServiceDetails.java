package com.example.demo.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.example.demo.model.Admin;
import com.example.demo.model.Role;

public class AdminServiceDetails extends User {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Admin admin;

	public AdminServiceDetails(Admin admin) {
		super(admin.getId(), admin.getPassword(), authorities(admin.getRoles()));
		this.admin = new Admin();
		// TODO Auto-generated constructor stub
	}
	
	private static Collection<? extends GrantedAuthority> authorities(Role role) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (role.getRole() == "ADMIN") {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        return authorities;
    }

    public Admin getAdmin() {
        return admin;
    }
	
	
}
