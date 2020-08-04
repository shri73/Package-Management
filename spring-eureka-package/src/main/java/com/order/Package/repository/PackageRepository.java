package com.order.Package.repository;

import java.util.List;
import java.util.Optional;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import com.order.Package.model.ListPackages;
import com.order.Package.model.Package;

@EnableScan
public interface PackageRepository extends CrudRepository<Package, String>{
	Optional<Package> findById(String trackingNumber);
	List<Package> findByUsername(String username);
	List<Package> findByStoreId(String storeId);
	Iterable<Package> findAll();
	
}
