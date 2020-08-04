package com.order.Package.repository;

import java.util.Optional;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import com.order.Package.model.Beacon;
import com.order.Package.model.Store;

@EnableScan
public interface BeaconRepository extends CrudRepository<Beacon, String> {
	
	Optional<Beacon> findByStore(Store store);
	

}
