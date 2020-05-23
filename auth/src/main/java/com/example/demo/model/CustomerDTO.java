package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class CustomerDTO {
	 private String name;
	 
	 public String toString() {
			StringBuilder builder = new StringBuilder();
	        builder.append("{\"name\" :");
	        builder.append(name);
	        builder.append("}");
	    return builder.toString();
			
		}

}
