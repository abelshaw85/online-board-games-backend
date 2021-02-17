package com.online.board.games.model;

public class Drop extends Action {
	private RowColPosition dropPos;
	private String droppingColour;
	private String droppingPieceName;
	
	public Drop() {
		super("Drop");
	}

	public Drop(RowColPosition dropPos, String droppingColour, String droppingPieceName) {
		this();
		this.dropPos = dropPos;
		this.droppingColour = droppingColour;
		this.droppingPieceName = droppingPieceName;
	}

	public RowColPosition getDropPos() {
		return dropPos;
	}

	public void setDropPos(RowColPosition dropPos) {
		this.dropPos = dropPos;
	}

	public String getDroppingColour() {
		return droppingColour;
	}

	public void setDroppingColour(String droppingColour) {
		this.droppingColour = droppingColour;
	}

	public String getDroppingPieceName() {
		return droppingPieceName;
	}

	public void setDroppingPieceName(String droppingPieceName) {
		this.droppingPieceName = droppingPieceName;
	}

	@Override
	public String toString() {
		return "Drop [dropPos=" + dropPos + ", droppingColour=" + droppingColour + ", droppingPieceName=" + droppingPieceName
				+ "]";
	}
}
