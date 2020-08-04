package com.order.Package.feign;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.order.Package.model.PushNotificationRequest;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
public class NotificationFallbackService implements NotificationClient{

	@Override
	public ResponseEntity sendDataNotification(PushNotificationRequest request) {
		log.info("Notification couldnt be sent for " + request.getToken());
		return null;
	}

}
