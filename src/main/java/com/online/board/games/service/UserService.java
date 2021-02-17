package com.online.board.games.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.online.board.games.dto.UserDTO;
import com.online.board.games.entity.User;

public interface UserService extends UserDetailsService {
	public User findByUserName(String userName);
	public void save(UserDTO userDto);
}
