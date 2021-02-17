package com.online.board.games.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Square {
	@JsonProperty("p")
	private Piece piece;
	
	public Square() {
	}
	
	public Square(Square that) {
		this(that.getPiece() == null ? null : new Piece(that.getPiece()));
	}

	public Square(Piece piece) {
		this.piece = piece;
	}

	public Piece getPiece() {
		return piece;
	}

	public void setPiece(Piece piece) {
		this.piece = piece;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Square [piece=");
		if (this.piece == null) {
			sb.append("null");
		} else {
			sb.append(piece.toString());
		}
		sb.append("]");
		return sb.toString();
	}
}
