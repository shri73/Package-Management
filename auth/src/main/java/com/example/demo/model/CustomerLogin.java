package com.example.demo.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerLogin {

	@Email(message = "Please provide a valid Email")
    @NotEmpty(message = "Please provide an email")
    private String email;
	
    @Length(min = 5, message = "*Your password must have at least 5 characters")
    @NotEmpty(message = "Please provide your password")
    private String password;
    
    public String toString() {
		StringBuilder builder = new StringBuilder();
        builder.append("{\"email\" :");
        builder.append(email);
        builder.append(", \"password\" :");
        builder.append(password);
        builder.append("}");
    return builder.toString();
		
	}
}