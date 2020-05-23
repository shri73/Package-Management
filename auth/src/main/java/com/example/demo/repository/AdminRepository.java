package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Admin;
import com.example.demo.model.Store;

@Repository
public interface AdminRepository extends CrudRepository<Admin, String> {
	
	Admin findByStore(String storeNumber);

}
