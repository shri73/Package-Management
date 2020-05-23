package com.example.demo.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.AESenc;
import com.example.demo.model.Admin;
import com.example.demo.model.AdminDTO;
import com.example.demo.model.AdminLogin;
import com.example.demo.model.Customer;
import com.example.demo.model.CustomerDTO;
import com.example.demo.model.CustomerLogin;
import com.example.demo.model.Decrypt;
import com.example.demo.service.AdminService;
import com.example.demo.service.CustomerService;
import com.example.demo.utils.Utils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value="Auth API", description="Operations pertaining to Authentication & Authorization")
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success"),
        @ApiResponse(code = 201, message = "Successfully registerd customer"),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
        @ApiResponse(code = 500, message = "Unexpetced error")
})
public class LoginControlller {

	@Autowired
    private CustomerService userService;
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	Utils util;
	
	@PostMapping(path = "/customer/register")
	@ApiOperation(value = "Register a customer", response = HttpStatus.class)
	public ResponseEntity<String> register(@Valid @RequestBody Decrypt request) {
		 Customer user = util.decryptCustomer(request.getDecryptString());
	     userService.saveCustomer(user);
		 return new ResponseEntity<String>(HttpStatus.CREATED);
	}
	
	@PostMapping(path = "/customer/login")
	@ApiOperation(value = "Login for a customer", response = CustomerDTO.class)
	public ResponseEntity<String> customerLogin(@Valid @RequestBody Decrypt request) throws Exception {
		CustomerLogin login =  util.decryptCustomerLogin(request.getDecryptString());
		CustomerDTO sendCustomerDTO = userService.loginUser(login);
		String sendString = AESenc.encrypt(sendCustomerDTO.toString());
		return ResponseEntity.ok(sendString);
	}
	
	
	@PostMapping(path = "/token")
	public ResponseEntity<String> getToken( @Valid @RequestBody Object user) throws Exception {
		String sendString = AESenc.encrypt(user.toString());
		return ResponseEntity.ok(sendString);
	}
	
	@PostMapping(path = "/decrypted")
	public ResponseEntity<Object> getObject( @Valid @RequestBody Decrypt request) throws Exception {
		Object sendString =  util.decryptObject(request.getDecryptString()); 
		return ResponseEntity.ok(sendString);
	}
	
	
     @PostMapping(path = "/admin/register/{storeNumber}") 
     @ApiOperation(value = "Register a Admin", response = AdminDTO.class)
	 public ResponseEntity<String> registerAdminWithEncryption(@PathVariable("storeNumber") String storeNumber, @Valid @RequestBody Decrypt request) throws Exception { 
		  Admin user = util.decryptAdminRequest(request.getDecryptString()); 
		  AdminDTO sendAdmin = adminService.saveAdmin(user, storeNumber); 
		  String sendString = AESenc.encrypt(sendAdmin.toString());
		  return ResponseEntity.ok(sendString); 
   }
     
    @PostMapping(path = "/admin/login")
 	@ApiOperation(value = "Login for a Admin", response = AdminDTO.class)
 	public ResponseEntity<String> adminLogin(@Valid @RequestBody Decrypt request) throws Exception {
 		AdminLogin login = util.decryptAdminLogin(request.getDecryptString());
 		AdminDTO sendAdmin = adminService.loginUser(login);
 		String sendString = AESenc.encrypt(sendAdmin.toString());
 		return ResponseEntity.ok(sendString);
 	}  
	 
	
	
	
	
}
