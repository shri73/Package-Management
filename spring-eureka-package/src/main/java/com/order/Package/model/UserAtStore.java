package com.order.Package.model;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("User at Store model")
public class UserAtStore {
	
	public String username;
	
	public Beacon beacon;
	

}
