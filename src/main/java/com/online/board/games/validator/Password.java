package com.online.board.games.validator;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = PasswordValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {
	public String value() default "";
	
	public String message() default "ERROR: Invalid Password";
	
	public Class<?>[] groups() default {};
	
	public Class<? extends Payload>[] payload() default {};

}
