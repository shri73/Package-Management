package com.eureka.auth.service;

import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eureka.auth.exception.BadRequestException;
import com.eureka.auth.exception.ResourceNotFoundException;
import com.eureka.auth.model.Admin;
import com.eureka.auth.model.AdminDTO;
import com.eureka.auth.model.AdminDeviceToken;
import com.eureka.auth.model.AdminLogin;
import com.eureka.auth.model.Beacon;
import com.eureka.auth.model.BeaconId;
import com.eureka.auth.model.Role;
import com.eureka.auth.model.Store;
import com.eureka.auth.repository.AdminRepository;
import com.eureka.auth.repository.RoleRepository;


@Service
public class AdminService{
	
	private AdminRepository repository;
	
	private StoreService service;
	
    private RoleRepository roleRepository;
	
    private BeaconService beaconService;
	
    private PasswordEncoder passwordEncoder;
	
	private ModelMapper modelMapper;
	
	public AdminService(AdminRepository repository, StoreService service, 
			BeaconService beaconService,
			RoleRepository roleRepository, @Lazy PasswordEncoder passwordEncoder, 
			ModelMapper modelMapper  ) {
		
		this.repository = repository;
		this.service = service;
		this.roleRepository = roleRepository;
		this.beaconService = beaconService;
		this.passwordEncoder = passwordEncoder;
		this.modelMapper = modelMapper;
	}
	
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
	
	private Admin AssignStoreToAdmin(String storeNumber, Admin admin) {
		Optional<Store> store = service.findStoreByStoreNumber(storeNumber);
		Store retrievedStore = null;
		if(store.isPresent()) {
			 retrievedStore= store.get();
			 if(retrievedStore.getManager() == null) {
				 retrievedStore.setManager(admin);
				 admin.setStore(retrievedStore);
				 repository.save(admin);
			 }
			 else {
				 throw new BadRequestException("The Store Number " + storeNumber + " has manager assigned");
			 }
			 
		}
		return admin;
		
	}
	
	public AdminDTO loginUser(AdminLogin user) {
		Optional<Admin> userExists = findByEmployeeID(user.getEmployeeId());
		Admin retrievedAdmin = null;
		AdminDTO sendAdmin = new AdminDTO();
		if(userExists.isPresent()) {
			retrievedAdmin = userExists.get();
			if(null != retrievedAdmin.getDeviceToken() && !retrievedAdmin.getDeviceToken().contains(user.getDeviceToken())) {
				retrievedAdmin.getDeviceToken().add(user.getDeviceToken());
				repository.save(retrievedAdmin);
			}
			
		}
		else {
			throw new BadRequestException("Invalid employee ID.");
		}

		String password = user.getPassword();
		if (!passwordEncoder.matches(password, retrievedAdmin.getPassword())) {
			throw new BadRequestException("Invalid user name and password combination.");
		}

		
		sendAdmin = modelMapper.map(retrievedAdmin, AdminDTO.class);
		return sendAdmin;
	}

    
    public AdminDTO saveAdmin(@NotNull Admin user, String storeNumber) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByRole("ADMIN");
        user.setRoles(userRole);
        Admin savedAdmin = AssignStoreToAdmin(storeNumber, user);
        AdminDTO sendAdmin = new AdminDTO();
        sendAdmin = modelMapper.map(savedAdmin, AdminDTO.class);
        return sendAdmin;
    }




	public void updateToken(@NotNull AdminDTO login) {
		Optional<Admin> userExists = findByEmployeeID(login.getUsername());
		Admin retrievedAdmin = null;
		
		if(userExists.isPresent()) {
			retrievedAdmin = userExists.get();
			if(null != retrievedAdmin.getDeviceToken() && retrievedAdmin.getDeviceToken().contains(login.getDeviceToken())) {
				retrievedAdmin.getDeviceToken().remove(login.getDeviceToken());
				repository.save(retrievedAdmin);
			}
		}
		else {
			throw new BadRequestException("Invalid employee ID.");
		}
		
		
		
	}


	public AdminDeviceToken getDeviceToken(@Valid Beacon request) {
		BeaconId id = new BeaconId();
		id = modelMapper.map(request, BeaconId.class);
		Store store = beaconService.findByBeacon(id);
        Admin sendAdmin = null;
        AdminDeviceToken token = new AdminDeviceToken();
		if(store != null) {
			Optional<Admin> adminOptional = findByStoreNumber(store.getStoreNumber());
			if(adminOptional.isPresent()) {
				sendAdmin = adminOptional.get();
				token = modelMapper.map(sendAdmin, AdminDeviceToken.class);
			}
		}
		else {
			throw new BadRequestException("Invalid Beacon.");
		}
		return token;
		
	}


	

}
