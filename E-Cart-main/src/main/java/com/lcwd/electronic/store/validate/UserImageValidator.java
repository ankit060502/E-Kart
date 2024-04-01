package com.lcwd.electronic.store.validate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserImageValidator implements ConstraintValidator<UserImageValid, String>{

	
	private Logger logger = LoggerFactory.getLogger(UserImageValidator.class);   
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		// TODO Auto-generated method stub
		
		logger.info("mesage from is valid{}",value);
		// logic
		if(value.isBlank()) {
			return false;
		}else {
			return true;
		}
		
	}

}
