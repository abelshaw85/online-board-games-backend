package com.online.board.games.model;

public class GameDetail {
	private int gameId;
	private String type;
	private String status;
	private Player player1;
	private Player player2;
	
	public GameDetail(int gameId, String type, String status, Player player1, Player player2) {
		this.gameId = gameId;
		this.type = type;
		this.status = status;
		this.player1 = player1;
		this.player2 = player2;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Player getPlayer1() {
		return player1;
	}

	public void setPlayer1(Player player1) {
		this.player1 = player1;
	}

	public Player getPlayer2() {
		return player2;
	}

	public void setPlayer2(Player player2) {
		this.player2 = player2;
	}

	@Override
	public String toString() {
		return "GameDetail [gameId=" + gameId + ", type=" + type + ", status=" + this.status + " player1=" + player1 + ", player2=" + player2
				+ "]";
	}

}
