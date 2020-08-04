package com.eureka.auth.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminLogin {
	
	
    private String employeeId;
	
    @Length(min = 5, message = "*Your password must have at least 5 characters")
    @NotEmpty(message = "Please provide your password")
    private String password;
    
    @NotNull(message = "Please provide device token")
    private String deviceToken;
    
    public String toString() {
		StringBuilder builder = new StringBuilder();
        builder.append("{\"employeeId\" :");
        builder.append(employeeId);
        builder.append(", \"password\" :");
        builder.append(password);
        builder.append(", \"deviceToken\" :");
        builder.append(deviceToken);
        builder.append("}");
    return builder.toString();
		
	}

}
