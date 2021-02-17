package com.online.board.games.model;

public class Move extends Action {
	private RowColPosition from;
	private RowColPosition to;
	
	public Move() {
		super("Move");
	}

	public Move(RowColPosition from, RowColPosition to) {
		this();
		this.from = from;
		this.to = to;
	}

	public RowColPosition getFrom() {
		return from;
	}

	public void setFrom(RowColPosition from) {
		this.from = from;
	}

	public RowColPosition getTo() {
		return to;
	}

	public void setTo(RowColPosition to) {
		this.to = to;
	}

	@Override
	public String toString() {
		return "Move [from=" + from + ", to=" + to + "]";
	}	
}
