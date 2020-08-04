package com.eureka.auth.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eureka.auth.exception.StoreNotFoundException;
import com.eureka.auth.model.Store;
import com.eureka.auth.repository.StoreRepository;
@Service
public class StoreService {
	
	private StoreRepository repository;
	
	public StoreService(StoreRepository repository) {
		this.repository = repository;
	}
	
	public Optional<Store> findStoreByStoreNumber(String storeNumber) {
		return Optional.ofNullable(repository.findByStoreNumber(storeNumber).
				orElseThrow(() -> new StoreNotFoundException("Store number " + storeNumber + " does not exist")));		
	}
	
	public void saveStore(Store store) {
		repository.save(store);
	}
	
	
	

}
