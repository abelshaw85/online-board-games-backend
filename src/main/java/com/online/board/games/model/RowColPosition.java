package com.online.board.games.model;

public class RowColPosition {
	private int row;
	private int col;
	
	public RowColPosition() {
	}
	
	public RowColPosition(int row, int col) {
		this.row = row;
		this.col = col;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	@Override
	public String toString() {
		return "RowColPosition [row=" + row + ", col=" + col + "]";
	}
	
}
