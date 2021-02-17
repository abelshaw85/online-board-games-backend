package com.online.board.games.model;

import java.util.ArrayList;
import java.util.List;

public class Turn {
	private int gameId;
	private String player;
	private List<Action> actions;
	
	public Turn() {
		this.actions = new ArrayList<>();
	}
	
	public Turn(int gameId) {
		super();
		this.gameId = gameId;
	}
	
	public Turn(int gameId, String player) {
		this(gameId);
		this.player = player;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public String getPlayer() {
		return player;
	}

	public void setPlayer(String player) {
		this.player = player;
	}

	public Turn(List<Action> actions) {
		this.actions = actions;
	}
	
	public void addAction(Action action) {
		if (this.actions == null) {
			this.actions = new ArrayList<>();
		}
		this.actions.add(action);
	}

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}

	@Override
	public String toString() {
		return "Turn [gameId=" + gameId + ", player=" + player + ", actions=" + actions + "]";
	}
}
