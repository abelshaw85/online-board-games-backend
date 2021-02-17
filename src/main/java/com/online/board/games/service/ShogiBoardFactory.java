package com.online.board.games.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.online.board.games.model.Game;
import com.online.board.games.model.Piece;
import com.online.board.games.model.Square;

/* Shogi Game Factory with default board setup for creating new games */
@Service
public class ShogiBoardFactory {
	
	private Game game;

	public ShogiBoardFactory() {
		String fileName = "/data/shogi-board.json";
		String data = "";
		
		try {
			// Use of Input Stream allows for both IDE and JAR resource finding. Otherwise the JSON file would not be accessible.
			InputStream in = getClass().getResourceAsStream(fileName); 
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			
			data = reader.lines().collect(Collectors.joining());
			ObjectNode node = new ObjectMapper().readValue(data, ObjectNode.class);
			int boardSize = node.get("Size").asInt();
			this.game = new Game(boardSize);
			this.game.setType("Shogi");
			this.game.setStatus("Ongoing");
			if (node.get("Black").isArray()) {
				for (JsonNode objNode : node.get("Black")) {
					Piece piece = new Piece(objNode.get("pieceName").textValue(), "Black");
					int row = objNode.get("position").get("row").asInt();
					int col = objNode.get("position").get("col").asInt();
					if (piece.getName().equals("SHO-Pawn")) {
			            for (; col < boardSize; col++) {
			              this.game.setPiece(piece, row, col);
			            }
			        } else {
			        	this.game.setPiece(piece, row, col);
			        }
				}
			}
			//copy rows 0 and 3, as these are identical for both sides of the board
			Square[][] gameSquares = this.game.getSquares();
	        Square[] endRow = gameSquares[boardSize - 1];
	        Square[] frontRow = gameSquares[boardSize - 3];
	        for (int col = 0; col < endRow.length; col++) {
	        	gameSquares[0][col].setPiece(new Piece(endRow[col].getPiece().getName(), "White")); //true makes the piece move downwards
	        	gameSquares[2][col].setPiece(new Piece(frontRow[col].getPiece().getName(), "White"));
	        }
	        // manually set the middle row, as this varies slightly from the black version
	        if (node.get("White").isArray()) {
	        	for (JsonNode objNode : node.get("White")) {
					Piece piece = new Piece(objNode.get("pieceName").textValue(), "White");
					int row = objNode.get("position").get("row").asInt();
					int col = objNode.get("position").get("col").asInt();
			        this.game.setPiece(piece, row, col);
				}
	        }
	        game.setActiveColour("Black"); // Black goes first
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	* Returns a new Shogi game with pieces in their starting positions.
	*/
	public Game newGame() {
		//returns a copy of the default game setup
		return new Game(this.game);
	}

}
