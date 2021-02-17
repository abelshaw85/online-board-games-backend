package com.online.board.games.validator;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = UserNameValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UserName {
	public String value() default "tt";
	
	public String message() default "ERROR: Invalid Username";
	
	public Class<?>[] groups() default {};
	
	public Class<? extends Payload>[] payload() default {};

}
