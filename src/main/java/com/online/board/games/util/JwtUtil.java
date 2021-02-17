package com.online.board.games.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/*
 Utility class to generate, verify and extract data from JWTs.
*/
@Service
public class JwtUtil {
	private String SECRET_KEY = "secret";
	
	public String extractUsername(String token) {
		return this.extractClaim(token, Claims::getSubject);
	}
	
	public Date extractExpiration(String token) {
		return this.extractClaim(token, Claims::getExpiration);
	}
	
	// Method can be used to extract information from the Jwt such as the username (subject) or expiration date
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = this.extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	private Claims extractAllClaims(String token) {
		return Jwts.parser()
				.setSigningKey(SECRET_KEY)
				.parseClaimsJws(token)
				.getBody();
	}
	
	private Boolean isTokenExpired(String token) {
		
		return this.extractExpiration(token).before(new Date());
	}
	
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return this.createToken(claims, userDetails.getUsername());
	}	
	private String createToken(Map<String, Object> claims, String subject) {
		// Claims: Anything else that can be included in the payload
		// Subject: The username of the user being authenticated
		// IssuedAt: Time token is issued
		// Set expiration: When the token will expire, in this case 10hrs (1000 milliseconds, times 60 is a minute, times 60 is an hour, times 10 is 10hrs)
		// SignWith: Use a secret key and an algorithm to generate the token
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY)
				.compact();
	}
	
	// Returns true if the username is correct and the token has not yet expired.
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = this.extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !this.isTokenExpired(token));
	}
}
