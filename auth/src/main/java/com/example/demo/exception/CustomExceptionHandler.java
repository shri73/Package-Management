package com.example.demo.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.demo.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.extern.slf4j.Slf4j;



@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
	
	@Autowired
	Utils util;

	@ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleException(BadRequestException ex) throws JsonProcessingException {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("Bad Request", details);
        
        String jsonString = util.objectToJson(error);
        log.error(jsonString);
        return new ResponseEntity<Object>(util.encryptObject(jsonString), HttpStatus.BAD_REQUEST);
    }
	
	public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
	       String error = "Malformed JSON request";
	       List<String> details = new ArrayList<>();
	       details.add(ex.getLocalizedMessage());
	       ErrorResponse errorResponse = new ErrorResponse(error, details);
	       String jsonString = null;
			try {
				jsonString = util.objectToJson(error);
			} catch (JsonProcessingException e) {
				log.error(errorResponse.toString());
			}
	       
	       return new ResponseEntity<Object>(util.encryptObject(jsonString), HttpStatus.BAD_REQUEST);
	   }
	
	@ExceptionHandler(InternalServerError.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex) throws JsonProcessingException {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("Server Error", details);
        String jsonString = util.objectToJson(error);
        log.error(error.toString());
        return new ResponseEntity<Object>(util.encryptObject(jsonString), HttpStatus.INTERNAL_SERVER_ERROR);
    }
 
    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) throws JsonProcessingException {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("No Resource Found", details);
        String jsonString = util.objectToJson(error);
        log.error(error.toString());
        return new ResponseEntity<Object>(util.encryptObject(jsonString), HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(StoreNotFoundException.class)
    public final ResponseEntity<Object> handleStoreNotFoundException(StoreNotFoundException ex) throws JsonProcessingException {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("No Store Found", details);
        String jsonString = util.objectToJson(error);
        log.error(error.toString());
        return new ResponseEntity<Object>(util.encryptObject(jsonString), HttpStatus.NOT_FOUND);
    }
 

}
