package com.order.Package;

import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.repository.query.QueryLookupStrategy.Key;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ComponentScan(basePackages = {"com.order.Package"})
@EnableDynamoDBRepositories(queryLookupStrategy = Key.CREATE_IF_NOT_FOUND)
@EnableEurekaClient
@EnableFeignClients
@EnableAsync
public class PackageApplication {

	public static void main(String[] args) {
		SpringApplication.run(PackageApplication.class, args);
	}

}
