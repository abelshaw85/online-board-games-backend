package com.online.board.games.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserNameValidator implements ConstraintValidator<UserName, String>
{
	//must contain letters, underscore or hyphen and be no longer than 15 chars
	private String userNameRegex = "[\\w_-]{1,15}";

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return value == null || value.matches(userNameRegex);
	}
}
