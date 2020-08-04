package com.order.Package.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.order.Package.model.Beacon;
import com.order.Package.model.Store;
import com.order.Package.model.Stores;
import com.order.Package.repository.BeaconRepository;

@Service
public class BeaconService {
	
	BeaconRepository repository;
	
	public BeaconService(BeaconRepository repository) {
		this.repository = repository;
	}
	
	public Beacon getBeaconByStore(String storeID) {
		Store store = new Store(storeID);
		Optional<Beacon> beaconOptional = repository.findByStore(store);
		return beaconOptional.isPresent() ? beaconOptional.get() : null;
		
	}

}
