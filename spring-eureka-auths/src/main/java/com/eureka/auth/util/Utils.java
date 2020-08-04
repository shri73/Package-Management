package com.eureka.auth.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

@Component
public class Utils {
	
	@Autowired
	Gson gson;
	
	public String objectToJson(Object obj) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
        String jsonTarget = mapper.writeValueAsString(obj);
		return jsonTarget;

	}

}
