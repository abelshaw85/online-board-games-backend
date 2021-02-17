package com.online.board.games.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Piece {
	@JsonProperty("n")
	private String name;
	@JsonProperty("c")
	private String colour;
	
	public Piece() {
	}
	
	public Piece(Piece that) {
	    this(that.getName(), that.getColour());
	}

	public Piece(String name, String colour) {
		this.name = name;
		this.colour = colour;
	}

	public String getColour() {
		return colour;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Piece [name=" + name + ", colour=" + colour + "]";
	}
}
