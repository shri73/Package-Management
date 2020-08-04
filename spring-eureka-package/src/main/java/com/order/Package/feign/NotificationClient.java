package com.order.Package.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.order.Package.model.PushNotificationRequest;


@FeignClient(value = "notification", fallback=NotificationFallbackService.class )
public interface NotificationClient {
	
	 @PostMapping("/notification/data")
	  public ResponseEntity sendDataNotification(@RequestBody PushNotificationRequest request);

}
