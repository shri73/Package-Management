package com.eureka.auth.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.eureka.auth.model.Admin;
import com.eureka.auth.model.AdminDTO;
import com.eureka.auth.model.AdminDeviceToken;
import com.eureka.auth.model.AdminLogin;
import com.eureka.auth.model.Beacon;
import com.eureka.auth.model.Customer;
import com.eureka.auth.model.CustomerDTO;
import com.eureka.auth.model.CustomerDeviceToken;
import com.eureka.auth.model.CustomerEmail;
import com.eureka.auth.model.CustomerLogin;
import com.eureka.auth.service.AdminService;
import com.eureka.auth.service.CustomerService;
import com.eureka.auth.util.Utils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "Auth API", description = "Operations pertaining to Authentication & Authorization")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 201, message = "Successfully registerd customer"),
		@ApiResponse(code = 400, message = "Bad request"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 500, message = "Unexpetced error") })
public class LoginControlller {

	@Autowired
	private CustomerService userService;
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private Utils util;


	@PostMapping(path = "/customer/register")
	@ApiOperation(value = "Register a customer", response = HttpStatus.class)
	public ResponseEntity<String> register(@Valid @RequestBody Customer user) {
		userService.saveCustomer(user);
		return new ResponseEntity<String>(HttpStatus.CREATED);
	}

	@PostMapping(path = "/customer/login")
	@ApiOperation(value = "Login for a customer", response = CustomerDTO.class)
	public ResponseEntity<CustomerDTO> customerLogin(@Valid @RequestBody CustomerLogin request) throws Exception {

		CustomerDTO sendCustomerDTO = userService.loginUser(request);
		return ResponseEntity.ok(sendCustomerDTO);
	}

	@PostMapping(path = "/admin/register/{storeNumber}")
	@ApiOperation(value = "Register a Admin", response = AdminDTO.class)
	public ResponseEntity<AdminDTO> registerAdminWithEncryption(@PathVariable("storeNumber") String storeNumber,
			@Valid @RequestBody Admin request) throws Exception {

		AdminDTO sendAdmin = adminService.saveAdmin(request, storeNumber);

		return ResponseEntity.ok(sendAdmin);
	}

	@PostMapping(path = "/admin/login")
	@ApiOperation(value = "Login for a Admin", response = AdminDTO.class)
	public ResponseEntity<AdminDTO> adminLogin(@Valid @RequestBody AdminLogin request) throws Exception {

		AdminDTO sendAdmin = adminService.loginUser(request);
		return ResponseEntity.ok(sendAdmin);
	}

	@PostMapping(path = "/admin/logout")
	@ApiOperation(value = "Update device token")
	public ResponseEntity<String> adminUpdateToken(@Valid @RequestBody AdminDTO request) throws Exception {

		adminService.updateToken(request);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@PostMapping(path = "/admin/deviceToken")
	@ApiOperation(value = "send device token of admin", response = AdminDeviceToken.class)
	public AdminDeviceToken adminDeviceToken(@Valid @RequestBody Beacon request) throws Exception {
		AdminDeviceToken admin = adminService.getDeviceToken(request);
		return admin;
	}

	@PostMapping(path = "/customer/deviceToken")
	@ApiOperation(value = "send device token of customer", response = CustomerDTO.class)
	public CustomerDeviceToken customerDeviceToken(@Valid @RequestBody CustomerEmail request) throws Exception {
		CustomerDeviceToken user = userService.getDeviceToken(request.getUsername());
		return user;
	}

	@PostMapping(path = "/customer/logout")
	@ApiOperation(value = "send device token of customer", response = CustomerDTO.class)
	public ResponseEntity<String> customerLogout(@Valid @RequestBody CustomerDTO request) throws Exception {

		userService.logout(request);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

}
