package com.lcwd.electronic.store.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	
	private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponseMessage> resourceNotFoundExceptionHandler(ResourceNotFoundException exception){
		
		logger.info("exception handler invoked");
		
		ApiResponseMessage response = ApiResponseMessage.builder()
				.message(exception.getMessage())
				.success(true)
				.status(HttpStatus.NOT_FOUND).build();
		return new ResponseEntity(response,HttpStatus.NOT_FOUND);
		
	}
	
	// MethodArgumentNotValidException
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String,Object>> handelMethodArgumentNotValidException(MethodArgumentNotValidException exception){
		
		
		List<ObjectError>  allErrors = exception.getBindingResult().getAllErrors();
		Map<String,Object> response = new HashMap<>();
		allErrors.stream().forEach(objectError->{
			
			String message = objectError.getDefaultMessage();
			String field = ((FieldError)objectError).getField();
			response.put(field,message);
			
		}); 
		
		return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
		
		
	} 
	
	// handerl api bad api request 
	@ExceptionHandler(ImageBadApiRequest.class)
	public ResponseEntity<ApiResponseMessage> imageBadApiRequestHandler(ImageBadApiRequest exception){
		
		logger.info("Bad api Request");
		
		ApiResponseMessage response = ApiResponseMessage.builder()
				.message(exception.getMessage())
				.success(false)
				.status(HttpStatus.BAD_REQUEST).build();
		return new ResponseEntity(response,HttpStatus.BAD_REQUEST);
		
	}

}
