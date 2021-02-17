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
public class ChessBoardFactory {
	
	private Game game;

	public ChessBoardFactory() {
		String fileName = "/data/chess-board.json";
		String data = "";
		
		try {
			// Use of Input Stream allows for both IDE and JAR resource finding. Otherwise the JSON file would not be accessible.
			InputStream in = getClass().getResourceAsStream(fileName); 
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			
			data = reader.lines().collect(Collectors.joining());
			ObjectNode node = new ObjectMapper().readValue(data, ObjectNode.class);
			int boardSize = node.get("Size").asInt();
			this.game = new Game(boardSize);
			this.game.setType("Chess");
			this.game.setStatus("Ongoing");
			
			// Setup White pieces using JSON
			if (node.get("White").isArray()) {
				for (JsonNode objNode : node.get("White")) {
					Piece piece = new Piece(objNode.get("pieceName").textValue(), "White");
					int row = objNode.get("position").get("row").asInt();
					int col = objNode.get("position").get("col").asInt();
					if (piece.getName().equals("CHE-Pawn")) {
			            for (; col < boardSize; col++) {
			              this.game.setPiece(piece, row, col);
			            }
			        } else {
			        	this.game.setPiece(piece, row, col);
			        }
				}
			}
			
			// Setup Black pieces by copying White and changing their colour
			Square[][] gameSquares = this.game.getSquares();
	        Square[] endRow = gameSquares[boardSize - 1];
	        Square[] frontRow = gameSquares[boardSize - 2];
	        // As queen/king are aligned, no need to reverse pieces etc
	        for (int col = 0; col < frontRow.length; col++) {
	        	gameSquares[0][col].setPiece(new Piece(endRow[col].getPiece().getName(), "Black"));
	        	gameSquares[1][col].setPiece(new Piece(frontRow[col].getPiece().getName(), "Black"));
	        }
	        game.setActiveColour("White"); // White goes first
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
