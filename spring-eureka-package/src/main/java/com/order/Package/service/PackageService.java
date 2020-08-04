package com.order.Package.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.order.Package.exception.BadRequestException;
import com.order.Package.exception.ErrorResponse;
import com.order.Package.exception.ResourceNotFoundException;
import com.order.Package.exception.StoreNotFoundException;
import com.order.Package.feign.AuthClient;
import com.order.Package.feign.NotificationClient;
import com.order.Package.model.AdminDeviceToken;
import com.order.Package.model.Beacon;
import com.order.Package.model.CustomerDeviceToken;
import com.order.Package.model.CustomerEmail;
import com.order.Package.model.ListPackages;
import com.order.Package.model.Package;
import com.order.Package.model.PushNotificationRequest;
import com.order.Package.model.Status;
import com.order.Package.model.StatusComparator;
import com.order.Package.model.Store;
import com.order.Package.model.UserAtStore;
import com.order.Package.repository.BeaconRepository;
import com.order.Package.repository.PackageRepository;
import com.order.Package.utils.Constants;
import com.order.Package.utils.Utils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PackageService {

	private PackageRepository packageRepo;

	private StoreService service;

	private AuthClient authClient;

	private Utils util;

	private NotificationClient notificationClient;
	
	private BeaconRepository beaconRepository;
	
	private BeaconService beaconService;
	
	public PackageService(PackageRepository packageRepo, StoreService service, 
			AuthClient authClient, Utils util, NotificationClient notificationClient,
			BeaconRepository beaconRepository, BeaconService beaconService ) {
		
		this.authClient = authClient;
		this.service = service;
		this.packageRepo = packageRepo;
		this.notificationClient = notificationClient;
		this.beaconRepository = beaconRepository;
		this.beaconService = beaconService;
		this.util = util;
		
	}
	
	public Package findByTrackingNumber(String trackingNumber) {
		Optional<Package> packageExistsOptional = packageRepo.findById(trackingNumber);
		if(null != packageExistsOptional && packageExistsOptional.isPresent()) {
			return packageExistsOptional.get();
		}
		else {
			log.info("Tracking number not found");
			return null;
		}
				
	}
	
	public ListPackages findPackageByUserEmail(String username) {
		List<Package> packageExistsOptional = packageRepo.findByUsername(username);
		if(null != packageExistsOptional && packageExistsOptional != null) {
			ListPackages packages = new ListPackages();
			packages.setLists(packageExistsOptional);
			return packages;
		}
		else {
			throw new ResourceNotFoundException(Constants.EMAIL_ERROR);
		}
	}
	
	public ListPackages findPackageByStoreID(String storeNumber) {
		List<Package> packageExistsOptional = packageRepo.findByStoreId(storeNumber);
		if(null != packageExistsOptional) {
			List<Package> filteredList = packageExistsOptional.stream().filter(p -> util.checkDateLimit(p.getDeliveryDate(), p.getStatus()) == false).collect(Collectors.toList());
			ListPackages packages = new ListPackages();
			packages.setLists(filteredList.stream().distinct().sorted(new StatusComparator()).collect(Collectors.toList()));
			return packages;
		}
		else {
			throw new ResourceNotFoundException(Constants.STORE_ERROR);
		}
	}
	
	public ListPackages findAllPackages() {
		Iterable<Package> packageExistsOptional = packageRepo.findAll();
		if(packageExistsOptional != null) {
			List<Package> list = StreamSupport 
                    .stream(packageExistsOptional.spliterator(), false) 
                    .collect(Collectors.toList()); 
			ListPackages packages = new ListPackages();
			packages.setLists(list);
			return packages;
		}
		else {
			throw new ResourceNotFoundException(Constants.PACKAGE_ERROR);
		}
	}
	
	public Package savePackage( @Valid Package toBeSaved) {
		if(findByTrackingNumber(toBeSaved.getTrackingNumber()) != null) {
			throw new BadRequestException(Constants.TRACKING_EXISTS);
		}
		
		if(service.StoreExists(toBeSaved.getStoreId())) {
			toBeSaved.setStatus(Status.IN_TRANSIT);
			packageRepo.save(toBeSaved);
			return toBeSaved;
		}
		else {
			throw new StoreNotFoundException(Constants.STORE_ERROR);
		}
		
		
	}
	
	public ErrorResponse traverseList(ListPackages packages, String scan) {
		ErrorResponse errorResponses = new ErrorResponse();
		packages.getLists().stream().forEach(p -> updateStatusPossesion(p.getTrackingNumber(), p.getStoreId(), errorResponses, scan, p.getDeliveryDate()));
		
		return errorResponses;
	}
	
	private void updateStatusPossesion(String trackingNumber, String storeId,  ErrorResponse errorResponse, String scan, LocalDate deliveryDate) {
		Package toBeUpdatedPackage = findByTrackingNumber(trackingNumber);
		Beacon beacon = beaconService.getBeaconByStore(storeId);
		List<String> details = null;
		if(errorResponse.getDetails() == null) {
			details = new ArrayList<>();
		}
		else {
			details = errorResponse.getDetails();
		}
		 
		errorResponse.setMessage(Constants.BAD_REQUEST);
		if(toBeUpdatedPackage != null) {
			if(!toBeUpdatedPackage.getStoreId().equalsIgnoreCase(storeId)) {
				details.add(trackingNumber + ": The StoreID does not match");
				errorResponse.setDetails(details);
			}
			else {
				if(scan.equalsIgnoreCase(Constants.POSSESSION)) {
					if(toBeUpdatedPackage.getStatus().name().equalsIgnoreCase(Status.IN_TRANSIT.getName())) {
						CustomerEmail requestCustomerEmail = new CustomerEmail();
						requestCustomerEmail.setUsername(toBeUpdatedPackage.getUsername());
						CustomerDeviceToken userDTO = getCustomerDeviceToken(requestCustomerEmail);
						toBeUpdatedPackage.setStatus(Status.IN_STORE);
						packageRepo.save(toBeUpdatedPackage);
						try {
							generateNotificationRequest(toBeUpdatedPackage, Constants.POSSESSION, userDTO, beacon.getUuid());
						} catch (Exception e) {
							details.add(trackingNumber + ": " + e.getMessage());
							errorResponse.setDetails(details);
						}
					}
					else {
						details.add(trackingNumber + ": The Status must be in In Transit");
						errorResponse.setDetails(details);
					}
				}
				else if(scan.equalsIgnoreCase(Constants.DELIVERED)) {
					if(toBeUpdatedPackage.getStatus().name().equalsIgnoreCase(Status.IN_STORE.getName())) {
						CustomerEmail requestCustomerEmail = new CustomerEmail();
						requestCustomerEmail.setUsername(toBeUpdatedPackage.getUsername());
						CustomerDeviceToken userDTO = getCustomerDeviceToken(requestCustomerEmail);
						toBeUpdatedPackage.setStatus(Status.DELIVERED);
						log.info(deliveryDate.toString());
						toBeUpdatedPackage.setDeliveryDate(deliveryDate);
						packageRepo.save(toBeUpdatedPackage);
						try {
							generateNotificationRequest(toBeUpdatedPackage, Constants.DELIVERED, userDTO, beacon.getUuid());
						} catch (Exception e) {
							details.add(trackingNumber + ": " + e.getMessage());
							errorResponse.setDetails(details);
						}
					}
					else {
						details.add(trackingNumber + ": The Status must be in In Store");
						errorResponse.setDetails(details);
					}
				}
				
			}
			
		}
		else {
			details.add(trackingNumber + ": " + Constants.TRACKING_ERROR);
			errorResponse.setDetails(details);
		}
	}
	
	private void generateNotificationRequest(Package toBeUpdatedPackage, String scan, CustomerDeviceToken userDTO, String uuid) throws Exception {
		PushNotificationRequest request = new PushNotificationRequest();
		if(userDTO !=  null) {
			
			try {
				
				request.setTitle("Hello " + toBeUpdatedPackage.getName());
				request.setUuid(uuid);
				request.setToken(userDTO.getDeviceToken());
				request.setType(scan);
				if(scan.equalsIgnoreCase(Constants.POSSESSION)) {
					request.setMessage("Your Package " + toBeUpdatedPackage.getTrackingNumber() + " is ready to be picked up at store: " + toBeUpdatedPackage.getStoreId());
				}
				else {
					request.setMessage("Your Package " + toBeUpdatedPackage.getTrackingNumber() + " is delivered at store: " + toBeUpdatedPackage.getStoreId());
				}
				
				
			} catch (Exception e) {
				log.error("error in generateNotificationRequest"+ e.getMessage());
				throw new Exception(e.getMessage());
		   }
		   sendNotification(request);	
		}
		
		  
		
	}

	@Async("asyncExecutor")
	public void sendNotification(PushNotificationRequest request){
		notificationClient.sendDataNotification(request);
	}
	
	
	
	
	public AdminDeviceToken getDeviceToken(Beacon becaon){
		AdminDeviceToken adminDTO = authClient.adminDeviceToken(becaon);
		return adminDTO;
		
	}
	
	
	public CustomerDeviceToken getCustomerDeviceToken(CustomerEmail request){
		CustomerDeviceToken userDTO = authClient.customerDeviceToken(request);
		return userDTO;
		
	}
	
	
	public ListPackages getAllPackages(String userEmail){
		ListPackages packages = findPackageByUserEmail(userEmail);
		return packages;
		
	}
	
	public void sendAdminNotification(List<Package> deliveredPackages, Set<String> deviceToken) {
		PushNotificationRequest request = new PushNotificationRequest();
		request.setToken(deviceToken);
		if(deliveredPackages != null && deliveredPackages.size() > 0) {
			String trackingNumber = deliveredPackages.stream().map(each -> each.getTrackingNumber()).collect(Collectors.joining(", "));
			request.setTitle(Constants.CUSTOMER + deliveredPackages.stream().findFirst().get().getName() + Constants.AT_STORE);
			request.setMessage(Constants.FOR_PACKAGE + trackingNumber);
			sendNotification(request);
		}
	}
	
	private void sendCustomerNotification(List<Package> deliveredPackages, Set<String> deviceToken, String storeId) {
		PushNotificationRequest request = new PushNotificationRequest();
		Beacon beacon = beaconService.getBeaconByStore(storeId);
		request.setToken(deviceToken);
		if(deliveredPackages != null && deliveredPackages.size() > 0 && beacon != null) {
			request.setTitle("You are here !");
			request.setMessage("Seems like you are here to pick up your Package");
			request.setDirections(beacon.getDirections());
			sendNotification(request);
		}
	}
	
	public List<Package> getDeliveryPackages(ListPackages packages, String storeNumber) {
		return packages.getLists().stream().
				filter(each -> each.getStoreId().equalsIgnoreCase(storeNumber)).
				filter(each -> each.getStatus().getName().equalsIgnoreCase(Constants.IN_STORE)).
				collect(Collectors.toList());
	}

	public void processUserAtStore(UserAtStore trackingPackage) throws Exception {
		AdminDeviceToken admin = getDeviceToken(trackingPackage.getBeacon());
		CustomerEmail emailObj = new CustomerEmail();
		emailObj.setUsername(trackingPackage.getUsername());
		CustomerDeviceToken customer = getCustomerDeviceToken(emailObj);
		ListPackages packages = getAllPackages(trackingPackage.getUsername());
		List<Package> deliveredPackages = null;
		
		if(admin != null && packages != null && customer != null) {
			
			try {
				
				deliveredPackages = getDeliveryPackages(packages, admin.getStoreNumber());
				sendAdminNotification(deliveredPackages, admin.getDeviceToken());
				sendCustomerNotification(deliveredPackages, customer.getDeviceToken(), admin.getStoreNumber());
				
			} catch (Exception e) {
					log.info("error in processing user at store"+ e.getMessage());
					throw new Exception(e.getMessage());
			}
			
			
		}
		
	}

	

	public void saveBeacon(Beacon request) {
		beaconRepository.save(request);
		
	}

}
