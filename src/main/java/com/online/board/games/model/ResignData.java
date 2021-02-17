package com.online.board.games.model;

public class ResignData {
	private int gameId;
	private String resigningPlayer;
	
	public ResignData() {}
	
	public ResignData(int gameId, String resigningPlayer) {
		this.gameId = gameId;
		this.resigningPlayer = resigningPlayer;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public String getResigningPlayer() {
		return resigningPlayer;
	}

	public void setResigningPlayer(String resigningPlayer) {
		this.resigningPlayer = resigningPlayer;
	}
}
