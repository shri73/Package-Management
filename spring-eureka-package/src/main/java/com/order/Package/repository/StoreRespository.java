package com.order.Package.repository;

import java.util.Optional;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.order.Package.model.Stores;

@EnableScan
public interface StoreRespository extends CrudRepository<Stores, String> {
	
	Optional<Stores> findById(String storeNumber);
	
	

}
