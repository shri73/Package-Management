package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class AdminDTO {
	
	 private String id;
	 private String name;
	 private String storeNumber;
	 
	 public String toString() {
			StringBuilder builder = new StringBuilder();
	        builder.append("{\"id\" :");
	        builder.append(id);
	        builder.append(", \"name\" :");
	        builder.append(name);
	        builder.append(", \"storeNumber\" :");
	        builder.append(storeNumber);
	        builder.append("}");
	    return builder.toString();
			
		}

}
