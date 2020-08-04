package com.eureka.auth;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableEurekaClient
public class SpringEurekaAuthApp {

	public static void main(String[] args) {
		SpringApplication.run(SpringEurekaAuthApp.class, args);
	}
	
	@Bean
	public ModelMapper modelMapper() {
	 return new ModelMapper();
	}
}
