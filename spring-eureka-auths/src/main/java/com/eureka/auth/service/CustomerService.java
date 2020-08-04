package com.eureka.auth.service;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eureka.auth.exception.BadRequestException;
import com.eureka.auth.model.Customer;
import com.eureka.auth.model.CustomerDTO;
import com.eureka.auth.model.CustomerDeviceToken;
import com.eureka.auth.model.CustomerLogin;
import com.eureka.auth.model.Role;
import com.eureka.auth.repository.CustomerRepository;
import com.eureka.auth.repository.RoleRepository;


@Service
public class CustomerService {

	private CustomerRepository userRepository;
	
    private RoleRepository roleRepository;
	
	private PasswordEncoder passwordEncoder;
	
	private ModelMapper modelMapper;
	
	public CustomerService(CustomerRepository userRepository, RoleRepository roleRepository, 
			@Lazy PasswordEncoder bCryptPasswordEncoder, ModelMapper modelMapper) {
		
		this.passwordEncoder = bCryptPasswordEncoder;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.modelMapper = modelMapper;
		
	}
	
	
	
	public CustomerDTO loginUser(CustomerLogin user) {
		Customer userExists = userRepository.findByUsername(user.getUsername());
		CustomerDTO sendCustomerDTO = new CustomerDTO();
		if (userExists == null) {
			throw new BadRequestException("Invalid user name.");
		}

		String password = user.getPassword();
		if (!passwordEncoder.matches(password, userExists.getPassword())) {
			throw new BadRequestException("Invalid user name and password combination.");
		}
		if(null != userExists.getDeviceToken() && !userExists.getDeviceToken().contains(user.getDeviceToken())) {
			userExists.getDeviceToken().add(user.getDeviceToken());
			userRepository.save(userExists);
		}
		
		
		
		sendCustomerDTO = modelMapper.map(userExists, CustomerDTO.class);
		
		return sendCustomerDTO;
	}

    
    public Customer saveCustomer(Customer user) {
    	Customer userExists = userRepository.findByUsername(user.getUsername());
    	if(userExists != null) {
    		throw new BadRequestException("The email address is already registered");
    	}
        user.setPassword(passwordEncoder.encode(user.getPassword()));
       
        Role userRole = roleRepository.findByRole("CUSTOMER");
        user.setRoles(userRole);
        return userRepository.save(user);
    }

	public CustomerDeviceToken getDeviceToken(String username) {
		Customer userExists = userRepository.findByUsername(username);
		CustomerDeviceToken sendCustomerDTO = new CustomerDeviceToken();
		if (userExists == null) {
			throw new BadRequestException("Invalid user name.");
		}
		
		ModelMapper modelMapper = new ModelMapper();
		sendCustomerDTO = modelMapper.map(userExists, CustomerDeviceToken.class);
		
		return sendCustomerDTO;
	}

	public void logout(CustomerDTO customer) {
		Customer userExists = userRepository.findByUsername(customer.getUsername());
		if (userExists == null) {
			throw new BadRequestException("Invalid user name.");
		}
		
		if(null != userExists.getDeviceToken() && userExists.getDeviceToken().contains(customer.getDeviceToken())) {
			userExists.getDeviceToken().remove(customer.getDeviceToken());
			userRepository.save(userExists);
		}
		
		
	}


	
}
