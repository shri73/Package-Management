package com.order.Package.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@DynamoDBTable(tableName = "store")
@ApiModel("Store model")
public class Stores {

	@DynamoDBHashKey
    private String storeNumber;
}
