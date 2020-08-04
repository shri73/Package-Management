package com.order.Package.feign;

import javax.validation.Valid;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.order.Package.model.AdminDeviceToken;
import com.order.Package.model.Beacon;
import com.order.Package.model.CustomerDTO;
import com.order.Package.model.CustomerDeviceToken;
import com.order.Package.model.CustomerEmail;



@FeignClient(value = "auth", fallback=AuthFallbackService.class )
public interface AuthClient {
	
	 @PostMapping(path = "/admin/deviceToken")
	 public AdminDeviceToken adminDeviceToken(@Valid @RequestBody Beacon request);
	 
	 @PostMapping(path = "/customer/deviceToken")
	 public CustomerDeviceToken customerDeviceToken(@Valid @RequestBody CustomerEmail request);
}
