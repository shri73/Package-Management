package com.order.Package.utils;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.order.Package.model.Status;

@Component
public class Utils {
	
	private static final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
        @Override
        public LocalDate deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return LocalDate.parse(json.getAsJsonPrimitive().getAsString());
        }
    }).create();
	
	
	
	public String objectToJson(Object obj) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
        String jsonTarget = mapper.writeValueAsString(obj);
		return jsonTarget;

	}
	
	
	public boolean checkDateLimit(LocalDate date, Status status) {
		if(date!= null && status.equals(Status.DELIVERED)) {
			LocalDate today = LocalDate.now();
		    // count number of days between the given date and today
		    long days = ChronoUnit.DAYS.between(date, today);
		    return days > 30;
		}
		return false;
	    
	}

}
