package com.online.board.games.entity;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {

	@Id
	@Column(name = "username")
	private String userName;

	@Column(name = "password")
	private String password;
	
	@Column(name = "enabled")
	private boolean enabled = true;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "users_roles", 
	joinColumns = @JoinColumn(name = "user_name"), 
	inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Collection<Role> roles;
	
	public User() {
		
	}
	
	public User(String userName, String password, Collection<Role> roles, boolean enabled) {
		this.userName = userName;
		this.password = password;
		this.roles = roles;
		this.enabled = enabled;
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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "User [userName=" + userName + ", password=" + password + ", roles=" + roles + "]";
	}
}
