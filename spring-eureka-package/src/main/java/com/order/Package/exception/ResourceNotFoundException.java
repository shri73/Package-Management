package com.order.Package.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)    
public class ResourceNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException (String message) {
		super(message);
	}
	
	public ResourceNotFoundException(String message, Throwable cause) {
		super (message, cause);
	}
}
