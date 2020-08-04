package com.order.Package.model;

import io.swagger.annotations.ApiModel;

@ApiModel("Status Enum")
public enum Status {
	IN_STORE("IN_STORE"),
	IN_TRANSIT("IN_TRANSIT"),
	DELIVERED("DELIVERED");
	

	
 private final String name;
    
    private Status(String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    @Override
    public String toString() {
        return this.name;
    }
    
}
