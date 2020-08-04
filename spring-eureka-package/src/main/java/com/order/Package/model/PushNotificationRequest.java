package com.order.Package.model;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PushNotificationRequest {

	 	private String title;
	    private String message;
	    private String topic;
	    private String uuid;
	    private String type;
	    private String directions;
	    private Set<String> token;

	    
	   
}
