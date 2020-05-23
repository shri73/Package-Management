package com.example.demo.exception;

import java.util.List;

public class ErrorResponse {
	public ErrorResponse(String message, List<String> details) {
        super();
        this.message = message;
        this.details = details;
    }
 
    //General error message about nature of error
    private String message;
 
    //Specific errors in API request processing
    private List<String> details;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getDetails() {
		return details;
	}

	public void setDetails(List<String> details) {
		this.details = details;
	}
	
	 @Override
	 public String toString() {
			StringBuilder builder = new StringBuilder();
	        builder.append("{\"message\" :");
	        builder.append(message);
	        builder.append(", \"details\" :");
	        builder.append(details);
	        builder.append("}");
	    return builder.toString();
			
		}


}
