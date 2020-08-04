package com.eureka.auth.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.eureka.auth.model.Customer;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {
	Customer findByUsername(String username);

}
