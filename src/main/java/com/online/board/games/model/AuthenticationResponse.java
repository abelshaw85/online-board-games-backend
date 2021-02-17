package com.online.board.games.model;

import java.util.Date;

/*
 * Used to return the JWT to the user upon successful authentication.
 */
public class AuthenticationResponse {
	private final String jwt;
	private final Date expiry;
	
	public AuthenticationResponse(String jwt, Date expiry) {
		this.jwt = jwt;
		this.expiry = expiry;
	}
	
	public String getJwt() {
		return this.jwt;
	}

	public Date getExpiry() {
		return expiry;
	}
}
