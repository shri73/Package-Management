package com.eureka.auth.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.eureka.auth.model.Beacon;
import com.eureka.auth.model.BeaconId;

@Repository
public interface BeaconRepository extends CrudRepository<Beacon, BeaconId>{

}
