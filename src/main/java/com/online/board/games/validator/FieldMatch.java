package com.online.board.games.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = FieldMatchValidator.class)
@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented // Adds Javadoc to this annotation.
public @interface FieldMatch {
	public String message() default "";
	
	public Class<?>[] groups() default {};
	
	public Class<? extends Payload>[] payload() default {};
	
	public String first();
	
    public String second();
    
    // Allows for several pairs of matches to be declared, e.g passwords, emails, captchas etc.
    @Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List
    {
    	FieldMatch[] value();
    }
}