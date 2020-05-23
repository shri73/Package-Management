package com.example.demo.utils;

import java.io.StringReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.AESenc;
import com.example.demo.model.Admin;
import com.example.demo.model.AdminLogin;
import com.example.demo.model.Customer;
import com.example.demo.model.CustomerLogin;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

@Component
public class Utils {
	
	@Autowired
	Gson gson;
	
	public Admin decryptAdminRequest(String request) {
		String toConvertString = "";
		
		Admin admin = null;
		try {
			toConvertString = AESenc.decrypt(request);
			admin = gson.fromJson(toConvertString, Admin.class);
		} catch (Exception e) {
			throw new BadRequestException("Something went wrong while decrypting");
		}
		return admin;
		
	}
	
	public Object encryptObject(Object toBeEncrypted) {
		try {
			return AESenc.encrypt(toBeEncrypted.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new BadRequestException("Something went wrong while encrypting");
		}
	}
	
	public AdminLogin decryptAdminLogin(String request) {
		AdminLogin convert = null;
		convert = gson.fromJson(decryptRequest(request), AdminLogin.class);
		return convert;
	}
	
	public Customer decryptCustomer(String request) {
		Customer convert = null;
		convert = gson.fromJson(decryptRequest(request), Customer.class);
		return convert;
	}
	
	public CustomerLogin decryptCustomerLogin(String request) {
		CustomerLogin convert = null;
		convert = gson.fromJson(decryptRequest(request), Customer.class);
		return convert;
	}
	
	public Object decryptObject(String request) throws JsonProcessingException {
		Object convert = null;
		//String jsonString = objectToJson(decryptRequest(request));
		convert = gson.fromJson(decryptRequest(request), Object.class);
		return convert;
	}
	
	public JsonReader decryptRequest(String request) {
		JsonReader reader = null;
		String toConvertString = "";
		try {
			toConvertString = AESenc.decrypt(request);
			reader = new JsonReader(new StringReader(toConvertString));
			reader.setLenient(true);
		} catch (Exception e) {
			throw new BadRequestException("Something went wrong while decrypting");
		}
		return reader;	
	}
	
	
	
	public String objectToJson(Object obj) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
        String jsonTarget = mapper.writeValueAsString(obj);
		return jsonTarget;

	}

}
