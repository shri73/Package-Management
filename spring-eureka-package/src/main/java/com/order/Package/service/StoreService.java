package com.order.Package.service;

import org.springframework.stereotype.Service;

import com.order.Package.repository.StoreRespository;

@Service
public class StoreService {
	
	private StoreRespository repository;
	
	public StoreService(StoreRespository repository) {
		this.repository = repository;
	}
	
	public boolean StoreExists(String storeNumber) {
		
		return repository.findById(storeNumber).isPresent();
		
	}
}
