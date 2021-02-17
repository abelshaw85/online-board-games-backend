package com.online.board.games.model;

public class Take extends Action {
	private String takingColour;
	private String takenPieceName;

	public Take() {
		super("Take");
	}

	public Take(String takingColour, String takenPiece) {
		this();
		this.takingColour = takingColour;
		this.takenPieceName = takenPiece;
	}

	public String getTakingColour() {
		return takingColour;
	}

	public void setTakingColour(String takingColour) {
		this.takingColour = takingColour;
	}

	public String getTakenPieceName() {
		return takenPieceName;
	}

	public void setTakenPieceName(String takenPiece) {
		this.takenPieceName = takenPiece;
	}

	@Override
	public String toString() {
		return "Take [takingColour=" + takingColour + ", takenPieceName=" + takenPieceName + "]";
	}
}
