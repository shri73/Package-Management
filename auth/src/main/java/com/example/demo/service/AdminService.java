package com.example.demo.service;

import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Admin;
import com.example.demo.model.AdminDTO;
import com.example.demo.model.AdminLogin;
import com.example.demo.model.Role;
import com.example.demo.model.Store;
import com.example.demo.repository.AdminRepository;
import com.example.demo.repository.RoleRepository;

@Service
@Transactional
public class AdminService implements UserDetailsService{
	
	@Autowired
	private AdminRepository repository;
	
	@Autowired
	private StoreService service;
	
	@Autowired
    private RoleRepository roleRepository;
	
	@Autowired
    PasswordEncoder passwordEncoder;
	
	ModelMapper modelMapper = new ModelMapper();
	
	public Optional<Admin> findByEmployeeID(String id) {
		return Optional.ofNullable(repository.findById(id).
				orElseThrow(() -> new ResourceNotFoundException("Employee ID " + id + "does not exist")));		
		
	}
	
	
	public Optional<Admin> findByStoreNumber(String storeNumber) {
		Optional<Store> store = service.findStoreByStoreNumber(storeNumber);
		Optional<Admin> assocatedAdmin = Optional.empty();
		if(store.isPresent()) {
			Store retrievedStore= store.get();
			assocatedAdmin = Optional.ofNullable(retrievedStore.getManager());
		}
		return assocatedAdmin;
		
	}
	
	private void AssignStoreToAdmin(String storeNumber, Admin admin) {
		Optional<Store> store = service.findStoreByStoreNumber(storeNumber);
		Store retrievedStore = null;
		if(store.isPresent()) {
			 retrievedStore= store.get();
			 if(retrievedStore.getManager() == null) {
				 retrievedStore.setManager(admin);
				 //service.saveStore(retrievedStore);
				 admin.setStore(retrievedStore);
			 }
			 else {
				 throw new BadRequestException("The Store Number " + storeNumber + " has manager assigned");
			 }
			 
			 //service.saveStore(retrievedStore);
		}
		
	}
	
	public AdminDTO loginUser(AdminLogin user) {
		Optional<Admin> userExists = findByEmployeeID(user.getEmployeeId());
		Admin retrievedAdmin = null;
		AdminDTO sendAdmin = new AdminDTO();
		if(userExists.isPresent()) {
			retrievedAdmin = userExists.get();
		}
		else {
			throw new BadRequestException("Invalid employee ID.");
		}

		String password = user.getPassword();
		if (!passwordEncoder.matches(password, retrievedAdmin.getPassword())) {
			throw new BadRequestException("Invalid user name and password combination.");
		}

		
		sendAdmin = modelMapper.map(retrievedAdmin, AdminDTO.class);
		/*
		 * sendAdmin.setId(retrievedAdmin.getId());
		 * sendAdmin.setName(retrievedAdmin.getName());
		 * sendAdmin.setStoreNumber(retrievedAdmin.getStore().getStoreNumber());
		 */
		return sendAdmin;
	}

    
    public AdminDTO saveAdmin(@NotNull Admin user, String storeNumber) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByRole("ADMIN");
        user.setRoles(userRole);
        AssignStoreToAdmin(storeNumber, user);
        Admin savedAdmin = repository.save(user);
        AdminDTO sendAdmin = new AdminDTO();
        sendAdmin = modelMapper.map(savedAdmin, AdminDTO.class);
        return sendAdmin;
    }


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Admin> admin = findByEmployeeID(username);
		
		if (!admin.isPresent()) {
            throw new UsernameNotFoundException(username);
        }
        return new AdminServiceDetails(admin.get());
		
	}


	

}
