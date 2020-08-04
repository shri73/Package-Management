package com.eureka.auth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AdminDTO {
	
	 private String username;
	 private String name;
	 private String storeNumber;
	 private String deviceToken;
	 public String toString() {
			StringBuilder builder = new StringBuilder();
	        builder.append("{\"username\" :");
	        builder.append(username);
	        builder.append(", \"name\" :");
	        builder.append(name);
	        builder.append(", \"storeNumber\" :");
	        builder.append(storeNumber);
	        builder.append(", \"deviceToken\" :");
	        builder.append(deviceToken);
	        builder.append("}");
	    return builder.toString();
			
		}

}
