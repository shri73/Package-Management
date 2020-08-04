package com.order.Package.model;

import java.time.LocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "package")
@ApiModel("Package model")
public class Package {
	
	@DynamoDBHashKey
    private String trackingNumber;
	
	@DynamoDBAttribute
	@Email
	@NotEmpty(message="A Email address is required")
    private String username;
	
    @DynamoDBTypeConvertedEnum
    private Status status;
    
    @DynamoDBAttribute
    private String name;
    
    @DynamoDBAttribute
    @NotNull(message="A Store Id is required")
    private String storeId;
	
    @DynamoDBAttribute
    @NotNull(message="A description is required")
    private String description;
    
	
    @DynamoDBTypeConverted( converter = LocalDateToStringTypeConverter.class )
	@DynamoDBAttribute 
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonIgnore
	private LocalDate deliveryDate;
	 
    
    public String toString() {
		StringBuilder builder = new StringBuilder();
        builder.append("{\"trackingNumber\" :");
        builder.append(trackingNumber);
        builder.append(", \"userEmail\" :");
        builder.append(username);
        builder.append(", \"status\" :");
        builder.append(status.toString());
        builder.append(", \"storeId\" :");
        builder.append(storeId);
        builder.append(", \"description\" :");
        builder.append(description);
        builder.append(", \"name\" :");
        builder.append(name);
		builder.append(", \"deliveryDate\" :"); builder.append(deliveryDate);
		 
        builder.append("}");
    return builder.toString();
		
	}
	
	/*
	 * @DynamoDBTypeConverted(converter = LocalDateConverter.class)
	 * 
	 * @DynamoDBAttribute private LocalDate birthDate;
	 */
	
	

}
