package com.online.board.games.model;

public class Action {
	protected String type;
	
	public Action() {
	}

	public Action(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}	
}
