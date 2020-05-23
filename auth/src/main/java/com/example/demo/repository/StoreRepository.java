package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Admin;
import com.example.demo.model.Store;
@Repository
public interface StoreRepository extends CrudRepository<Store, String> {
	
	Optional<Store> findByStoreNumber(String storeNumber);

}
