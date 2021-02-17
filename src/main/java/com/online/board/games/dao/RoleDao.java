package com.online.board.games.dao;

import com.online.board.games.entity.Role;

public interface RoleDao {

	public Role findRoleByName(String theRoleName);
	
}
