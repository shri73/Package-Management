package com.order.Package.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CustomerDTO {
	 private String name;
	 
	 private String deviceToken;
	 
	 public String toString() {
			StringBuilder builder = new StringBuilder();
	        builder.append("{\"name\" :");
	        builder.append(name);
	        builder.append(", \"deviceToken\" :");
	        builder.append(deviceToken);
	        builder.append("}");
	    return builder.toString();
			
		}

}
