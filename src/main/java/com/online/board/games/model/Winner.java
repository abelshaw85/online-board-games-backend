package com.online.board.games.model;

public class Winner extends Action {
	private String winnerName;
	
	public Winner() {
		super("Winner");
	}

	public Winner(String winnerName) {
		this();
		this.winnerName = winnerName;
	}

	public void setWinnerName(String winnerName) {
		this.winnerName = winnerName;
	}

	public String getWinnerName() {
		return this.winnerName;
	}

	@Override
	public String toString() {
		return "Winner [type=" + this.type + " winnerName=" + winnerName + "]";
	}
}
