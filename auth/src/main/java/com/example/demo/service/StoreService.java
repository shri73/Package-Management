package com.example.demo.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.StoreNotFoundException;
import com.example.demo.model.Store;
import com.example.demo.repository.StoreRepository;

@Service
@Transactional
public class StoreService {
	
	@Autowired
	private StoreRepository repository;
	
	public Optional<Store> findStoreByStoreNumber(String storeNumber) {
		return Optional.ofNullable(repository.findByStoreNumber(storeNumber).
				orElseThrow(() -> new StoreNotFoundException("Store number " + storeNumber + " does not exist")));		
	}
	
	public void saveStore(Store store) {
		repository.save(store);
	}
	
	
	

}
