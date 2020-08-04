# Package-Management-System

This case study provides a complete backend infrastructure for a software application responsible to streamline the Package Management System at stores after online purchases. 

So, when a customer having ordered a package enters the store to pick it up, both the customer and a designated employee on shift gets a notification. The employee gets the info that “Customer John, picking up package 123456789 has entered the store”, and the customer gets the info regarding where and how-to pick up the package.

## Tools and Technologies:
			
			Java 8
			Spring Boot 
			Spring Security
			Spring Cloud - Open Feign 
			Netflix Zuul
			Netflix Eureka Client/Server 
			Netflix Ribbon 
			Spring Data JPA
			SQL
			DynamoDB
			FireBase Messaging System
			JWT (using JJWT for token provider)
			ELK Stack (Log aggregation)
			FeignClient

## Services

![Core Services](/images/screenshot.png)

#### Notes
* Each microservice (auth & package) has it's own database and there is no way to access the database directly from other services.
* The auth service in this project is using MySQL for the persistent storage. And Package microservice is using AWS DynamoDB.
* Service-to-service communiation is done by using FeignClient (Declarative Http client, which integrates with Ribbon and Hystrix)

## Infrastructure
![Infrastructure](/images/screenshot2.png)

### Notes:
*  For the Package Management System, all requests from the client will be mediated by an API gateway who is responsible for load balancing access control and JWT validation (Using JJWT provider).
* Using Firebase Cloud Messaging to send notifications to users through their device token obtained when user logs in.
* Using ELK Stack for log aggregation.

![Infrastructure](/images/logstash.png)    ![Infrastructure](/images/logstash2.png)

