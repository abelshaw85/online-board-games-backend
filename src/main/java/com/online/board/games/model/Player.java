package com.online.board.games.model;

public class Player {
	private String username;
	private String colour;
	
	public Player() {
		this.username = "";
		this.colour = "";
	}
	
	public Player(String username, String colour) {
		this.username = username;
		this.colour = colour;
	}
	
	public Player(Player that) {
		this.username = that.getUsername();
		this.colour = that.getColour();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getColour() {
		return colour;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}

	@Override
	public String toString() {
		return "Player [username=" + username + ", colour=" + colour + "]";
	}

}
