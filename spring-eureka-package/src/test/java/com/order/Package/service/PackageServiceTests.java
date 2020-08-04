package com.order.Package.service;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.order.Package.exception.ErrorResponse;
import com.order.Package.exception.ResourceNotFoundException;
import com.order.Package.exception.StoreNotFoundException;
import com.order.Package.feign.AuthClient;
import com.order.Package.feign.NotificationClient;
import com.order.Package.model.Beacon;
import com.order.Package.model.CustomerDeviceToken;
import com.order.Package.model.CustomerEmail;
import com.order.Package.model.ListPackages;
import com.order.Package.model.Package;
import com.order.Package.model.Status;
import com.order.Package.repository.PackageRepository;
import com.order.Package.utils.Constants;
import com.order.Package.utils.Utils;


@SpringBootTest
@ExtendWith(value = { SpringExtension.class })
public class PackageServiceTests {
	
	@Mock
	PackageRepository repository;
	
	@Mock
	Utils util;
	
	@InjectMocks
	PackageService service;
	
	@Mock
	NotificationClient notify_Client;
	
	@Mock
	StoreService storeService;
	
	@Mock
	BeaconService beaconService;
	
	@Mock
	AuthClient authClient;
	
	Package packageObj;
	
	List<Package> packages;
	
	Beacon beacon;
	
	
	private static String TRACKING_NUMBER = "E123456789";
	private static String STORE_ID = "S113";
	private static String USER_EMAIL = "test@gmail.com";
	
	@BeforeEach
	public void setUp() {
		packageObj = new Package();
		packageObj.setTrackingNumber(TRACKING_NUMBER);
		packageObj.setStoreId(STORE_ID);
		packageObj.setUsername(USER_EMAIL);
		packageObj.setStatus(Status.IN_TRANSIT);
		packages = new ArrayList<>();
		packages.add(packageObj);
		beacon = new Beacon();
		
	}
	
	@Test
	public void testFindByTrackingId() {
		
		  Mockito.when(repository.findById(TRACKING_NUMBER)).thenReturn(Optional.
		  ofNullable(packageObj));
		  
		  Package getPackageOptional = service.findByTrackingNumber(TRACKING_NUMBER);
		  assertNotNull(getPackageOptional);
		  
		  Mockito.verify(repository, Mockito.times(1)).findById(Mockito.anyString());
		  Mockito.verifyNoMoreInteractions(repository);
		
	}
	
	@Test
	public void testFindByTrackingId_null() {
		 Mockito.when(repository.findById(TRACKING_NUMBER)).thenReturn(null);
		  
		  Package getPackage = service.findByTrackingNumber(TRACKING_NUMBER);
		  assertNull(getPackage);
		  
		  Mockito.verify(repository, Mockito.times(1)).findById(Mockito.anyString());
		  Mockito.verifyNoMoreInteractions(repository);
	}
	@Test
	public void testFindByUserEmail() {
		 Mockito.when(repository.findByUsername(USER_EMAIL)).thenReturn(packages);
		  
		  ListPackages getPackages = service.findPackageByUserEmail(USER_EMAIL);
		  assertNotNull(getPackages.getLists());
		  assertTrue(getPackages.getLists().size() > 0);
		  
		  Mockito.verify(repository, Mockito.times(1)).findByUsername(Mockito.anyString());
		  Mockito.verifyNoMoreInteractions(repository);
	}
	
	@Test
	public void testFindByUserEmail_null() {
		 Mockito.when(repository.findByUsername(USER_EMAIL)).thenReturn(null);
		  
		 Exception exception = assertThrows(ResourceNotFoundException.class, () ->{
			 service.findPackageByUserEmail(USER_EMAIL);
		 });
		  String actualMessage = exception.getMessage();
		  assertTrue(actualMessage.contains(Constants.EMAIL_ERROR));
		  
		  Mockito.verify(repository, Mockito.times(1)).findByUsername(Mockito.anyString());
		  Mockito.verifyNoMoreInteractions(repository);
	}
		
	@Test
	public void testFindByStoreId() {
		  Mockito.when(repository.findByStoreId(STORE_ID)).thenReturn(packages);
		  Mockito.when(util.checkDateLimit(null, Status.IN_TRANSIT)).thenReturn(false);
		  ListPackages getPackages = service.findPackageByStoreID(STORE_ID);
		  assertNotNull(getPackages.getLists());
		  assertTrue(getPackages.getLists().size() > 0);
		  
		  Mockito.verify(repository, Mockito.times(1)).findByStoreId(Mockito.anyString());
		  Mockito.verifyNoMoreInteractions(repository);
	}
	
	@Test
	public void testFindByStoreId_null() {
		Mockito.when(repository.findByStoreId("")).thenReturn(null);
		  
		 Exception exception = assertThrows(ResourceNotFoundException.class, () ->{
			 service.findPackageByStoreID("");
		 });
		  String actualMessage = exception.getMessage();
		  assertTrue(actualMessage.contains(Constants.STORE_ERROR));
		  
		  Mockito.verify(repository, Mockito.times(1)).findByStoreId(Mockito.anyString());
		  Mockito.verifyNoMoreInteractions(repository);
	}
	
	@Test
	public void testFindAll() {
		 Mockito.when(repository.findAll()).thenReturn(packages);
		  
		  ListPackages getPackages = service.findAllPackages();
		  assertNotNull(getPackages.getLists());
		  assertTrue(getPackages.getLists().size() > 0);
		  
		  Mockito.verify(repository, Mockito.times(1)).findAll();
		  Mockito.verifyNoMoreInteractions(repository);
	}
	
	@Test
	public void testFindAll_null() {
		 Mockito.when(repository.findAll()).thenReturn(null);
		  
		 Exception exception = assertThrows(ResourceNotFoundException.class, () ->{
			 service.findAllPackages();
		 });
		 String actualMessage = exception.getMessage();
		 assertTrue(actualMessage.contains(Constants.PACKAGE_ERROR));
		  
		  Mockito.verify(repository, Mockito.times(1)).findAll();
		  Mockito.verifyNoMoreInteractions(repository);
	}
	
	@Test
	public void testSave() {
		 Mockito.when(repository.save(packageObj)).thenReturn(packageObj);
		 Mockito.when(storeService.StoreExists(STORE_ID)).thenReturn(true); 
		  Package getPackages = service.savePackage(packageObj);
		  assertNotNull(getPackages);
		  assertTrue(getPackages.getStatus().equals(Status.IN_TRANSIT));
		  
		  Mockito.verify(repository, Mockito.times(1)).save(Mockito.anyObject());
		  Mockito.verifyNoMoreInteractions(repository);
	}
	
	@Test
	public void testSave_null() {
		 Mockito.when(repository.save(packageObj)).thenReturn(packageObj);
		 Mockito.when(storeService.StoreExists(STORE_ID)).thenReturn(false); 
		 Exception exception = assertThrows(StoreNotFoundException.class, () ->{
			 service.savePackage(packageObj);
		 });
		 String actualMessage = exception.getMessage();
		 assertTrue(actualMessage.contains(Constants.STORE_ERROR));
		  
		  Mockito.verify(repository, Mockito.times(0)).save(Mockito.anyObject());
		  Mockito.verifyNoMoreInteractions(repository);
	}
	
	@Test
	public void testUpdateStatus_Possession() {
		CustomerEmail reqEmail = new CustomerEmail();
		reqEmail.setUsername(USER_EMAIL);
		 Mockito.when(beaconService.getBeaconByStore(STORE_ID)).thenReturn(new Beacon());
		 Mockito.when(repository.findById(TRACKING_NUMBER)).thenReturn(Optional.
				  ofNullable(packageObj));
		 Mockito.when(authClient.customerDeviceToken(reqEmail)).thenReturn(new CustomerDeviceToken());
		 ListPackages listPackages = new ListPackages();
		 listPackages.setLists(packages);
		 ErrorResponse errorResponse = service.traverseList(listPackages, Constants.POSSESSION);
		 
		 assertNull(errorResponse.getDetails());
		  
		 Mockito.verify(authClient, Mockito.times(1)).customerDeviceToken(Mockito.anyObject());
		 Mockito.verify(notify_Client, Mockito.times(1)).sendDataNotification(Mockito.anyObject());
		 Mockito.verify(repository, Mockito.times(1)).save(Mockito.anyObject());
		 Mockito.verify(repository, Mockito.times(1)).findById(Mockito.anyString());
		 Mockito.verify(beaconService, Mockito.times(1)).getBeaconByStore(Mockito.anyString());
		 Mockito.verifyNoMoreInteractions(repository);
		 Mockito.verifyNoMoreInteractions(beaconService);
	}
	
	@Test
	public void testUpdateStatus_trackingNumberNotFound() {
		 Mockito.when(repository.findById(TRACKING_NUMBER)).thenReturn(Optional.
				  ofNullable(null));
		 ListPackages listPackages = new ListPackages();
		 listPackages.setLists(packages);
		 ErrorResponse errorResponse = service.traverseList(listPackages, Constants.POSSESSION);
		 
		 assertNotNull(errorResponse.getDetails());
		  
		  
		 Mockito.verify(repository, Mockito.times(0)).save(Mockito.anyObject());
		 Mockito.verify(repository, Mockito.times(1)).findById(Mockito.anyString());
		 Mockito.verifyNoMoreInteractions(repository);
	}
	
	@Test
	public void testUpdateStatus_207() {
		Package secondPackage = new Package();
		secondPackage.setTrackingNumber("1234");
		secondPackage.setStoreId("S111");
		
		Package thirdPackage = new Package();
		thirdPackage.setTrackingNumber("1234");
		thirdPackage.setStoreId("000");
		
		 Mockito.when(repository.findById(TRACKING_NUMBER)).thenReturn(Optional.
				  ofNullable(null));
		 Mockito.when(repository.findById("1234")).thenReturn(Optional.
				  ofNullable(secondPackage));
		 
		 
		 ListPackages listPackages = new ListPackages();
		 packages.add(thirdPackage);
		 listPackages.setLists(packages);
		 ErrorResponse errorResponse = service.traverseList(listPackages, Constants.POSSESSION);
		 
		 assertNotNull(errorResponse.getDetails());
		 assertTrue(errorResponse.getDetails().size() == 2); 
		  
		 Mockito.verify(repository, Mockito.times(0)).save(Mockito.anyObject());
		 Mockito.verify(repository, Mockito.times(2)).findById(Mockito.anyString());
		 Mockito.verifyNoMoreInteractions(repository);
	}
	
	@Test
	public void testUpdateStatus_save() {
		Package secondPackage = new Package();
		secondPackage.setTrackingNumber("1234");
		secondPackage.setStoreId("S111");
		
		Package thirdPackage = new Package();
		thirdPackage.setTrackingNumber("1234");
		thirdPackage.setStoreId("000");
		
		 Mockito.when(repository.findById(TRACKING_NUMBER)).thenReturn(Optional.
				  ofNullable(packageObj));
		 Mockito.when(repository.findById("1234")).thenReturn(Optional.
				  ofNullable(secondPackage));
		 
		 
		 ListPackages listPackages = new ListPackages();
		 packages.add(thirdPackage);
		 listPackages.setLists(packages);
		 ErrorResponse errorResponse = service.traverseList(listPackages, Constants.POSSESSION);
		 
		 assertNotNull(errorResponse.getDetails());
		 assertTrue(errorResponse.getDetails().size() > 0); 
		  
		 Mockito.verify(repository, Mockito.times(1)).save(Mockito.anyObject());
		 Mockito.verify(repository, Mockito.times(2)).findById(Mockito.anyString());
		 Mockito.verifyNoMoreInteractions(repository);
	}
	

	
	
	

}
