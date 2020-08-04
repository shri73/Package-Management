package com.order.Package.feign;

import javax.validation.Valid;

import org.springframework.stereotype.Component;

import com.order.Package.model.AdminDeviceToken;
import com.order.Package.model.Beacon;
import com.order.Package.model.CustomerDTO;
import com.order.Package.model.CustomerDeviceToken;
import com.order.Package.model.CustomerEmail;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
public class AuthFallbackService implements AuthClient {@Override
	public AdminDeviceToken adminDeviceToken(@Valid Beacon request) {
		log.info("Getting beacon {} {} {}", request.getUuid(), request.getMinor(), request.getMajor());
		log.info("error while calling auth service");
		return null;
	}

@Override
public CustomerDeviceToken customerDeviceToken(@Valid CustomerEmail request) {
	log.info("Getting email {}", request.getUsername());
	log.info("error while calling auth service");
	return null;
}


}
