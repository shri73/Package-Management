package com.order.Package.model;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDeviceToken {
	
	 private String id;
	 private String name;
	 private String storeNumber;
	 private Set<String> deviceToken;

}
