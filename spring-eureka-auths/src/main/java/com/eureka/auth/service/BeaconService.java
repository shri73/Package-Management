package com.eureka.auth.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eureka.auth.model.Beacon;
import com.eureka.auth.model.BeaconId;
import com.eureka.auth.model.Store;
import com.eureka.auth.repository.BeaconRepository;

@Service
public class BeaconService {
	
	
	private BeaconRepository repository;
	
	public BeaconService(BeaconRepository repository){
		this.repository = repository;
	}
	
	public Store findByBeacon(BeaconId request) {
		Optional<Beacon> getBeaconOptional = repository.findById(request);
		Store store= null;
		if(getBeaconOptional.isPresent()) {
			Beacon beacon = getBeaconOptional.get();
			store = beacon.getStore();
		}
		return store;
		
	}

}
