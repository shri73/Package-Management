package com.eureka.auth.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.eureka.auth.model.Store;

@Repository
public interface StoreRepository extends CrudRepository<Store, String> {
	
	Optional<Store> findByStoreNumber(String storeNumber);

}
