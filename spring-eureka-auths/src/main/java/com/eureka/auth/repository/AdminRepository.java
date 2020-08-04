package com.eureka.auth.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.eureka.auth.model.Admin;


@Repository
public interface AdminRepository extends CrudRepository<Admin, String> {
	
	Admin findByStore(String storeNumber);

}
