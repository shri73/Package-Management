package com.order.Package.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.order.Package.exception.BadRequestException;
import com.order.Package.exception.ErrorResponse;
import com.order.Package.model.Beacon;
import com.order.Package.model.CustomerEmail;
import com.order.Package.model.ListPackages;
import com.order.Package.model.Package;
import com.order.Package.model.Stores;
import com.order.Package.model.UserAtStore;
import com.order.Package.service.PackageService;
import com.order.Package.utils.Constants;
import com.order.Package.utils.Utils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/package")
@Api(value = "Package Management", description = "Operations pertaining to ordering and manging of Packages")
@ApiResponses(value = { @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 500, message = "Unexpetced error") })
public class PackageController {

	private PackageService service;

	public PackageController(PackageService service ) {
		this.service = service;
	}
	

	@PostMapping(value = "/order")
	@ApiOperation(value = "Order a Package")
	@ApiResponses(value = {
			@ApiResponse(code = 201, response = String.class, message = "Successfully created"),
			@ApiResponse(code = 400, message = Constants.BAD_REQUEST, response = BadRequestException.class) })
	public ResponseEntity<Package> saveOrder(@Valid @RequestBody Package request) throws Exception {
		
		Package savedPackage = service.savePackage(request);
		return new ResponseEntity<Package>(savedPackage, HttpStatus.CREATED);
	}
	
	@PostMapping(value = "/add/beacon")
	@ApiOperation(value = "Add beacon")
	@ApiResponses(value = {
			@ApiResponse(code = 201,  message = "Successfully created"),
			@ApiResponse(code = 400, message = Constants.BAD_REQUEST, response = BadRequestException.class) })
	public ResponseEntity<String> saveBeacon(@RequestBody @Valid Beacon request) throws Exception {
		
		service.saveBeacon(request);
		return new ResponseEntity<String>("Success", HttpStatus.CREATED);
	}

	@GetMapping(value = "/all")
	@ApiOperation(value = "Get all Packages")
	@ApiResponses(value = {
			@ApiResponse(code = 200, response = Package.class, message = "Successfully retrieved", responseContainer = "List"),
			@ApiResponse(code = 400, message = Constants.BAD_REQUEST, response = ErrorResponse.class) })
	public ResponseEntity<ListPackages> getAllPackages() throws Exception {
		ListPackages allPackages = service.findAllPackages();
		
		return new ResponseEntity<ListPackages>(allPackages, HttpStatus.OK);

	}

	@PostMapping(value = "/all/storeNumber")
	@ApiOperation(value = "Get all Packages with StoreNumber")
	@ApiResponses(value = {
			@ApiResponse(code = 200, response = Package.class, message = "Successfully retrieved", responseContainer = "List"),
			@ApiResponse(code = 400, message = Constants.BAD_REQUEST, response = ErrorResponse.class) })
	public ResponseEntity<ListPackages> getAllPackagesInStore(@Valid @RequestBody Stores request) throws Exception {
		
		ListPackages allPackagesForStore = service.findPackageByStoreID(request.getStoreNumber());
		
		return new ResponseEntity<ListPackages>(allPackagesForStore, HttpStatus.OK);

	}
	
	@PostMapping(value = "/all/email")
	@ApiOperation(value = "Get all Packages with user email address")
	@ApiResponses(value = {
			@ApiResponse(code = 200, response = Package.class, message = "Successfully retrieved", responseContainer = "List"),
			@ApiResponse(code = 400, message = Constants.BAD_REQUEST, response = ErrorResponse.class) })
	public ResponseEntity<ListPackages> getAllPackagesByEmail(@Valid @RequestBody CustomerEmail request) throws Exception {
		
		ListPackages allPackagesForStore = service.findPackageByUserEmail(request.getUsername());
		
		return new ResponseEntity<ListPackages>(allPackagesForStore, HttpStatus.OK);

	}


	@PutMapping(value = "/possession/status/trackingNumber")
	@ApiOperation(value = "Update status of tracking number to IN_STORE")
	@ApiResponses(value = {
			@ApiResponse(code = 207, message = Constants.BAD_REQUEST, response = ErrorResponse.class) })
	
	public ResponseEntity<Object> updateStatus(@Valid @RequestBody ListPackages request) throws Exception {
		
		ErrorResponse response = service.traverseList(request, Constants.POSSESSION);
		
		if(null != response.getDetails() && response.getDetails().size() > 0) {
			
			return new ResponseEntity<Object>(response, HttpStatus.MULTI_STATUS);
		}
		else {
			
			return new ResponseEntity<Object>( HttpStatus.OK);
		}
	}

	@PutMapping(value = "/delivered/status/trackingNumber")
	@ApiOperation(value = "Update status of tracking number to DELIVERED")
	@ApiResponses(value = {
			@ApiResponse(code = 207, message = Constants.BAD_REQUEST, response = ErrorResponse.class) })
	public  ResponseEntity<Object> updateStatusDelivered(@Valid @RequestBody ListPackages request) throws Exception {
		
		ErrorResponse response = service.traverseList(request, Constants.DELIVERED);
	
		if(null != response.getDetails() && response.getDetails().size() > 0) {
			
			return new ResponseEntity<Object>(response, HttpStatus.MULTI_STATUS);
		}
		else {
			
			return new ResponseEntity<Object>(HttpStatus.OK);
		}

	}

	@PutMapping(value = "/user/at/store")
	@ApiOperation(value = "Send Notification when user at store")
	@ResponseStatus(HttpStatus.OK)
	public void userAtStore(@Valid @RequestBody UserAtStore request) throws Exception {
		service.processUserAtStore(request);
	}

}
