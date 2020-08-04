package com.eureka.auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO extends CustomerEmail{
	 private String name;
	 private String deviceToken;
	 
	 public String toString() {
			StringBuilder builder = new StringBuilder();
	        builder.append("{\"name\" :");
	        builder.append(name);
	        builder.append("{\"email\" :");
	        builder.append(this.getUsername());
	        builder.append(", \"deviceToken\" :");
	        builder.append(deviceToken);
	        builder.append("}");
	    return builder.toString();
			
		}

}
