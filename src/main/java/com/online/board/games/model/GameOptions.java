package com.online.board.games.model;

public class GameOptions {
	private String type;
	private boolean singlePlayer;
	
	public GameOptions(String type, boolean singlePlayer) {
		this.type = type;
		this.singlePlayer = singlePlayer;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isSinglePlayer() {
		return singlePlayer;
	}

	public void setSinglePlayer(boolean singlePlayer) {
		this.singlePlayer = singlePlayer;
	}

	@Override
	public String toString() {
		return "GameOptions [type=" + type + ", singlePlayer=" + singlePlayer + "]";
	}
}
