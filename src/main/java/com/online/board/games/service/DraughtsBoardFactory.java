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

/* Draughts Game Factory with default board setup for creating new games */
@Service
public class DraughtsBoardFactory {
	
	private Game game;

	public DraughtsBoardFactory() {
		String fileName = "/data/draughts-board.json";
		String data = "";
		
		try {
			// Use of Input Stream allows for both IDE and JAR resource finding. Otherwise the JSON file would not be accessible.
			InputStream in = getClass().getResourceAsStream(fileName); 
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			
			data = reader.lines().collect(Collectors.joining());
			ObjectNode node = new ObjectMapper().readValue(data, ObjectNode.class);
			int boardSize = node.get("Size").asInt();
			this.game = new Game(boardSize);
			this.game.setType("Draughts");
			this.game.setStatus("Ongoing");
			this.setPieces(node.get("White"), "White");
			this.setPieces(node.get("Black"), "Black");
	        game.setActiveColour("White"); // Black goes first
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setPieces(JsonNode node, String colour) {
		for (JsonNode objNode : node) {
			Piece piece = new Piece(objNode.get("pieceName").textValue(), colour);
			int row = objNode.get("position").get("row").asInt();
			int col = objNode.get("position").get("col").asInt();
	        this.game.setPiece(piece, row, col);
		}
	}

	/**
	* Returns a new Draughts game with pieces in their starting positions.
	*/
	public Game newGame() {
		//returns a copy of the default game setup
		return new Game(this.game);
	}

}
