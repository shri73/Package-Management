package com.order.Package.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBDocument
public class Store {
	
	
    private String storeNumber;
	
	
}
