package com.example.demo.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.AdminDTO;
import com.example.demo.model.Customer;
import com.example.demo.model.CustomerDTO;
import com.example.demo.model.CustomerLogin;
import com.example.demo.model.Role;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.RoleRepository;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository userRepository;
	
	@Autowired
    private RoleRepository roleRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	public Customer findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
	
	public CustomerDTO loginUser(CustomerLogin user) {
		Customer userExists = findUserByEmail(user.getEmail());
		CustomerDTO sendCustomerDTO = new CustomerDTO();
		if (userExists == null) {
			throw new BadRequestException("Invalid user name.");
		}

		String password = user.getPassword();
		if (!bCryptPasswordEncoder.matches(password, userExists.getPassword())) {
			throw new BadRequestException("Invalid user name and password combination.");
		}

		ModelMapper modelMapper = new ModelMapper();
		sendCustomerDTO = modelMapper.map(userExists, CustomerDTO.class);
		
		return sendCustomerDTO;
	}

    
    public Customer saveCustomer(Customer user) {
    	Customer userExists = findUserByEmail(user.getEmail());
    	if(userExists != null) {
    		throw new BadRequestException("The email address is alreday registered");
    	}
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
       
        Role userRole = roleRepository.findByRole("CUSTOMER");
        user.setRoles(userRole);
        return userRepository.save(user);
    }
}
