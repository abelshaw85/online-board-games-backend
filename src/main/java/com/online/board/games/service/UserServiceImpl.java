package com.online.board.games.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.online.board.games.dao.RoleDao;
import com.online.board.games.dao.UserDao;
import com.online.board.games.dto.UserDTO;
import com.online.board.games.entity.Role;
import com.online.board.games.entity.User;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public User findByUserName(String userName) {
		return userDao.findByUserName(userName);
	}

	@Override
	@Transactional
	public void save(UserDTO userDto) {
		User user = new User();
		 // Map DTO details to User
		user.setUserName(userDto.getUserName());
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));

		user.setRoles(Arrays.asList(roleDao.findRoleByName("ROLE_USER")));

		userDao.save(user);
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		User user = userDao.findByUserName(userName);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
				mapRolesToAuthorities(user.getRoles()));
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream()
			.map(role -> new SimpleGrantedAuthority(role.getName()))
				.collect(Collectors.toList());
	}
}
