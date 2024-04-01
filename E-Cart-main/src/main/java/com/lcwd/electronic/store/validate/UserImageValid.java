package com.lcwd.electronic.store.validate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy =UserImageValidator.class)
public @interface UserImageValid {
	
	
	//  error message
	String message() default "Invalid Image name!!";
	
	// represent group of constraints
	
	Class<?>[] groups() default {};
	
	// additional  information about annotation
	
	Class<?  extends Payload>[] payload() default {};
	

}
