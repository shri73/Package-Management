package com.order.Package.model;



import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("Beacon information")
@DynamoDBTable(tableName = "beacon")
public class Beacon {
	
	@DynamoDBHashKey
	private String major;
	
	@DynamoDBAttribute
	private String uuid;
	
	@DynamoDBAttribute
	private String minor;
	
	@DynamoDBAttribute
	private String directions;
	
	@DynamoDBAttribute
	private Store store;
	
	
}
