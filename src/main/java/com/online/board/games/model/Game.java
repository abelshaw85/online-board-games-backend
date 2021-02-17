package com.online.board.games.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Game {
	private int gameId;
	@JsonProperty("t")
	private String type;
	@JsonProperty("st")
	private String status;
	@JsonProperty("sq")
	private Square[][] squares;
	@JsonProperty("p1")
	private Player player1;
	@JsonProperty("p2")
	private Player player2;
	@JsonProperty("w")
	private String winnerName;
	@JsonProperty("tp")
	private List<Piece> takenPieces;
	@JsonProperty("ac")
	private String activeColour;
	
	public Game() {
		this.takenPieces = new ArrayList<>();
		this.player1 = new Player();
		this.player2 = new Player();
	}

	//Used to create a deep clone of the game with no references to the original
	public Game(Game that) {
		// Creates empty arrays/lists to be populated with copies of original's data
		this(new Square[that.getSquares().length][that.getSquares().length], 
				that.type,
				that.status,
	    		new Player(that.getPlayer1()), 
	    		new Player(that.getPlayer2()),
	    		that.winnerName,
	    		new ArrayList<Piece>());
		this.gameId = that.getGameId();
		this.type = that.getType();
		int boardSize = that.getSquares().length;
		Square[][] originalSquares = that.getSquares();
		for (int row = 0; row < boardSize; row++) {
			for (int col = 0; col < boardSize; col++) {
				//Copy original square, including copy of the piece
				this.squares[row][col] = new Square(originalSquares[row][col]);
			}
		}
		//copy of pieces taken
		that.getTakenPieces().stream().forEach((takenPiece) -> {
			this.takenPieces.add(new Piece(takenPiece));
		});
		this.activeColour = that.activeColour;
	    
	}
	
	public Game(Square[][] squares) {
		this();
		this.squares = squares;
	}
	
	public Game(int boardSize) {
		this(new Square[boardSize][boardSize]);
		for (int row = 0; row < boardSize; row++) {
			//List<Square> row = new ArrayList<>();
			for (int col = 0; col < boardSize; col++) {
				//row.add(new Square());
				this.squares[row][col] = new Square();
			}
		}
	}

	public Game(Square[][] squares, String type, String status, Player player1, Player player2, String winnerName, List<Piece> takenPieces) {
		this.type = type;
		this.status = status;
		this.squares = squares;
		this.player1 = player1;
		this.player2 = player2;
		this.winnerName = winnerName;
		this.takenPieces = takenPieces;
	}
	
	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Square[][] getSquares() {
		return squares;
	}

	public void setSquares(Square[][] squares) {
		this.squares = squares;
	}

	public Player getPlayer1() {
		return player1;
	}

	public void setPlayer1(Player player1) {
		this.player1 = player1;
	}

	public Player getPlayer2() {
		return player2;
	}

	public void setPlayer2(Player player2) {
		this.player2 = player2;
	}

	public String getWinnerName() {
		return winnerName;
	}

	public void setWinnerName(String winnerName) {
		this.winnerName = winnerName;
	}

	public List<Piece> getTakenPieces() {
		return this.takenPieces;
	}

	public void setTakenByWhite(List<Piece> takenPieces) {
		this.takenPieces = takenPieces;
	}
	
	public void makeTurn(Turn turn) {
		for (Action action: turn.getActions()) {
			switch(action.getType()) {
			case "Move":
				Move move = (Move)action;
				this.move(move);
				break;
			case "Take":
				Take take = (Take)action;
				this.take(take);
				break;
			case "Promote":
				Promote promote = (Promote)action;
				this.promote(promote);
				break;
			case "Drop":
				System.out.println("Drop action found");
				Drop drop = (Drop)action;
				this.drop(drop);
				break;
			case "Winner":
				Winner winner = (Winner)action;
				this.winner(winner);
				break;
			default:
				System.out.println("Unknown Action.");
				break;
			}
		}
	}
	
	private void move(Move move) {
		Square from = this.squares[move.getFrom().getRow()][move.getFrom().getCol()];
		Square to = this.squares[move.getTo().getRow()][move.getTo().getCol()];
		to.setPiece(from.getPiece());
		from.setPiece(null);
	}
	
	private void take(Take take) {
		this.takenPieces.add(new Piece(take.getTakenPieceName(), take.getTakingColour()));
	}
	
	private void promote(Promote promote) {
		Square squToPromote = this.squares[promote.getPromoteLocation().getRow()][promote.getPromoteLocation().getCol()];
		if (promote.getPromotionPieceName() == null) {
			squToPromote.setPiece(null);
		} else {
			squToPromote.getPiece().setName(promote.getPromotionPieceName());
		}
	}
	
	private void drop(Drop drop) {
		Square squToDropTo = this.squares[drop.getDropPos().getRow()][drop.getDropPos().getCol()];
		Piece pieceToDrop = new Piece(drop.getDroppingPieceName(), drop.getDroppingColour());
		//find the piece in the list and remove it
		for (int i = 0; i < this.takenPieces.size(); i++) {
			Piece takenPiece = this.takenPieces.get(i);
			if (takenPiece.getName().equals(drop.getDroppingPieceName()) && takenPiece.getColour().equals(drop.getDroppingColour())) {
				this.takenPieces.remove(i);
				break; //piece found, end loop to prevent other pieces being removed
			}
		}
		squToDropTo.setPiece(pieceToDrop);
	}
	
	private void winner(Winner winner) {
		this.winnerName = winner.getWinnerName();
		this.status = "Closed";
	}
	
	public void setPiece(Piece piece, int row, int col) {
		this.squares[row][col].setPiece(piece);
	}

	public String getActiveColour() {
		return activeColour;
	}

	public void setActiveColour(String activeColour) {
		this.activeColour = activeColour;
	}
	
	public void toggleTurn() {
		if (this.activeColour.equals(this.player1.getColour())) {
			this.activeColour = this.player2.getColour();
		} else {
			this.activeColour = this.player1.getColour();
		}
	}
	
	public Player getActivePlayer() {
		return this.activeColour.equals(this.player1.getColour()) ? this.player1 : this.player2;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Game:\n");
		sb.append("Type=");
		sb.append(this.type);
		sb.append("Status=");
		sb.append(this.status);
		sb.append("\nPlayer1=");
		sb.append(this.player1 == null ? "null" : this.player1);
		sb.append("\nPlayer2=");
		sb.append(this.player2 == null ? "null" : this.player2);
		sb.append("\nWinner:");
		sb.append(this.winnerName == null ? "undetermined" : this.winnerName);
		sb.append("\nActive Turn:");
		sb.append(activeColour);
		sb.append("\n[squares=\n");
		Arrays.stream(this.squares).forEach((row) -> {
			sb.append(Arrays.toString(row));
			sb.append("\n");
		});
		sb.append("]\n");
		sb.append("Taken Pieces=[");
		sb.append(this.takenPieces);
		sb.append("]\n");
		return sb.toString();
	}

}
