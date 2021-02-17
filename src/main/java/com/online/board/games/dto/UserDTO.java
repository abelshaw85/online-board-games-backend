package com.online.board.games.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.online.board.games.validator.FieldMatch;
import com.online.board.games.validator.Password;
import com.online.board.games.validator.UserName;

@FieldMatch.List({
    @FieldMatch(first = "password", second = "matchingPassword", message = "The password fields must match.")
})
public class UserDTO {
	@NotNull(message = "User name is required.")
	@NotEmpty(message = "User name is required.")
	@Size(min = 5, message = "User name must be 5 characters or longer.")
	@UserName(message = "Username can only contain numbers, letters, underscores and hyphens.")
	private String userName;

	@NotNull(message = "Password is required.")
	@Size(min = 8, message = "Password must be at least 8 characters.")
	@Password(message = "Password must contain combination of upper and lowercase letters, numbers, and special characters.")
	private String password;
	
	@NotNull(message = "Confirm password is required.")
	@Size(min = 1, message = "Confirm password is required.")
	private String matchingPassword;
	
	public UserDTO() {
		
	}

	public UserDTO(String userName, String password, String matchingPassword) {
		this.userName = userName;
		this.password = password;
		this.matchingPassword = matchingPassword;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMatchingPassword() {
		return matchingPassword;
	}

	public void setMatchingPassword(String matchingPassword) {
		this.matchingPassword = matchingPassword;
	}

	@Override
	public String toString() {
		return "UserDTO [userName=" + userName + ", password=" + password + ", matchingPassword=" + matchingPassword
				+ "]";
	}
}
