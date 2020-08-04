package com.order.Package.model;

import java.time.LocalDate;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

public class LocalDateToStringTypeConverter implements DynamoDBTypeConverter<String, LocalDate> {

	@Override
	public String convert(LocalDate localDate) {
		return localDate.toString();
	}

	@Override
	public LocalDate unconvert(String object) {
		return LocalDate.parse(object);
	}

}
