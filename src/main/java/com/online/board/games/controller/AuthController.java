package com.online.board.games.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online.board.games.dto.UserDTO;
import com.online.board.games.entity.User;
import com.online.board.games.model.AuthenticationRequest;
import com.online.board.games.model.AuthenticationResponse;
import com.online.board.games.model.ResponseMessage;
import com.online.board.games.service.UserService;
import com.online.board.games.util.JwtUtil;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtUtil jwtUtil; // Our custom JWT utility class
	
	@PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
		// Exception thrown if user sends bad credentials 
		try {
			this.authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							authenticationRequest.getUsername(),
							authenticationRequest.getPassword()));
		} catch (BadCredentialsException ex) {
			throw new Exception("Incorrect username or password.", ex);
		}
		// If the authentication is successful, create a Jwt token to return to the user
		final UserDetails userDetails = this.userService.loadUserByUsername(authenticationRequest.getUsername());
		final String jwt = this.jwtUtil.generateToken(userDetails);
		return ResponseEntity.ok(new AuthenticationResponse(jwt, this.jwtUtil.extractExpiration(jwt)));
    }
	
	@PostMapping("/registerUser")
	public ResponseMessage registerUser(@Valid @RequestBody UserDTO userDto,
			BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) {
			return new ResponseMessage("RegistrationError", "The data contained errors.", bindingResult.getAllErrors());
		}
		
		User user = userService.findByUserName(userDto.getUserName());
		if (user != null){
			return new ResponseMessage("RegistrationError", "That username is already in the database.");
		}
		this.userService.save(userDto);
		return new ResponseMessage("RegistrationSuccess", "User added to the database!");
	}
}
