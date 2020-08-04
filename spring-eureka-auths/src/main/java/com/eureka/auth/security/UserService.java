package com.eureka.auth.security;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.eureka.auth.model.Admin;
import com.eureka.auth.model.Customer;
import com.eureka.auth.repository.CustomerRepository;
import com.eureka.auth.service.AdminService;
import com.eureka.auth.service.UserServiceDetails;

@Service
public class UserService implements UserDetailsService {
	
	private AdminService adminService;
	private CustomerRepository repository;
	
	public UserService(AdminService adminService,CustomerRepository repository) {
		this.adminService = adminService;
		this.repository = repository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		if(username.startsWith("EMP")) {
			Optional<Admin> adminOptional = adminService.findByEmployeeID(username);
			
			if (!adminOptional.isPresent()) {
	            throw new UsernameNotFoundException(username);
	        }
			
			Admin admin = adminOptional.get();
	        return new UserServiceDetails(admin.getUsername(), admin.getPassword(), admin.getRoles());
		}
		else {
			Optional<Customer> customerOptional = Optional.ofNullable(repository.findByUsername(username));
			
			if (!customerOptional.isPresent()) {
	            throw new UsernameNotFoundException(username);
	        }
			
			Customer customer = customerOptional.get();
			
	        return new UserServiceDetails(customer.getUsername(), customer.getPassword(), customer.getRoles());
		}

		
	}

}
