package com.order.Package.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.order.Package.utils.Constants;
import com.order.Package.utils.Utils;

import lombok.extern.slf4j.Slf4j;



@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
	
		private Utils util;
		
		public CustomExceptionHandler(Utils util) {
			this.util = util;
		}
	
	   @Override
	   protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
	                 HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> details = ex.getBindingResult()
			    .getFieldErrors()
			    .stream()
			    .map(x -> x.getDefaultMessage())
			    .collect(Collectors.toList());

        ErrorResponse error = new ErrorResponse("Invalid Input", details);
        String jsonString = null;
		try {
			jsonString = util.objectToJson(error);
			log.info(jsonString);
		} catch (JsonProcessingException e) {
			log.error(e.getMessage());
		}
        
        return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
	  }


	@ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleException(BadRequestException ex) throws JsonProcessingException {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse(Constants.BAD_REQUEST, details);
        String jsonString = util.objectToJson(error);
        log.info(jsonString);
        return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
    }
	
	public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
	       String error = "Malformed JSON request";
	       List<String> details = new ArrayList<>();
	        details.add(ex.getLocalizedMessage());
	       ErrorResponse errorResponse = new ErrorResponse(error, details);
	       String jsonString = "";
		try {
			jsonString = util.objectToJson(errorResponse);
		} catch (JsonProcessingException e) {
			log.error(jsonString);
		}
		   log.info(jsonString); 
	       return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
	   }
	
	@Override
	  protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

		  List<String> details = new ArrayList<>();
		  details.add(ex.getLocalizedMessage()); ErrorResponse error = new
		  ErrorResponse("Server Error", details);
		  
		   log.info(ex.getLocalizedMessage());
		  
		  return new ResponseEntity<Object>(error,
		  HttpStatus.INTERNAL_SERVER_ERROR);

	  }
 
    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) throws JsonProcessingException {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("No Resource Found", details);
        String jsonString = util.objectToJson(error);
        log.info(jsonString);
        return new ResponseEntity<Object>(error, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(StoreNotFoundException.class)
    public final ResponseEntity<Object> handleStoreNotFoundException(StoreNotFoundException ex) throws JsonProcessingException {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("No Store Found", details);
        String jsonString = util.objectToJson(error);
        log.info(jsonString);
        return new ResponseEntity<Object>(error, HttpStatus.NOT_FOUND);
    }
 

}
