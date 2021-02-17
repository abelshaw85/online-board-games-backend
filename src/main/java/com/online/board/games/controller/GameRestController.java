package com.online.board.games.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.online.board.games.bean.GameManager;
import com.online.board.games.model.Drop;
import com.online.board.games.model.Game;
import com.online.board.games.model.GameDetail;
import com.online.board.games.model.GameOptions;
import com.online.board.games.model.Move;
import com.online.board.games.model.Player;
import com.online.board.games.model.Promote;
import com.online.board.games.model.ResignData;
import com.online.board.games.model.ResponseMessage;
import com.online.board.games.model.Take;
import com.online.board.games.model.Turn;
import com.online.board.games.model.Winner;
import com.online.board.games.service.ChessBoardFactory;
import com.online.board.games.service.DraughtsBoardFactory;
import com.online.board.games.service.ShogiBoardFactory;

@RestController
public class GameRestController {
	
	@Autowired
	private GameManager gameManager;
	@Autowired
	private ShogiBoardFactory shogiBoardFactory;
	@Autowired
	private ChessBoardFactory chessBoardFactory;
	@Autowired
	private DraughtsBoardFactory draughtsBoardFactory;
	@Autowired
	private SimpMessagingTemplate webSocket;
	
	@Value("${origins.url}")
	private String stringValue;
	
	@GetMapping(value="/getGame/{gameId}")
	public ResponseMessage getGame(@PathVariable int gameId) {
		Game game = this.gameManager.getGame(gameId);
		if (game == null) {
			return new ResponseMessage("FetchGameError", "Error fetching game with id=" + gameId + ": Invalid ID.");
		}
		return new ResponseMessage("FetchGameSuccess", "Retrieved game with id=" + gameId, game);
	}
	
	@PostMapping("/newGame")
    public ResponseMessage newGame(Principal principal, @RequestBody GameOptions gameOptions) throws Exception {
		Game newGame = null;
		
		switch (gameOptions.getType()) {
			case "Shogi":
				newGame = this.shogiBoardFactory.newGame();
				break;
			case "Chess":
				newGame = this.chessBoardFactory.newGame();
				break;
			case "Draughts":
				newGame = this.draughtsBoardFactory.newGame();
				break;
			default:
				return new ResponseMessage("NewGameFailure", "Unknown game type.", null);
		}
		newGame.setPlayer1(new Player(principal.getName(), "White"));
		if (gameOptions.isSinglePlayer()) {
			newGame.setPlayer2(new Player(principal.getName(), "Black"));
			newGame.setStatus("Ongoing");
		} else {
			newGame.setPlayer2(new Player("", "Black"));
			newGame.setStatus("Awaiting");
		}
		
		// set both players as player1's name to enable single player testing!
		newGame.setPlayer1(new Player(principal.getName(), "White"));
		
		this.gameManager.addGame(newGame);
		GameDetail newGameDetail = this.gameManager.getGameDetailsById(newGame.getGameId());
		// Return the details of the new game
		return new ResponseMessage("NewGameSuccess", "New game added.", newGameDetail);
    }
	
	@PostMapping("/makeTurn")
    public ResponseMessage makeTurn(Principal principal, @RequestBody String jsonTurnData) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode node = new ObjectMapper().readValue(jsonTurnData, ObjectNode.class);
		int gameId = Integer.valueOf(node.get("gameId").textValue());
		
		Player activePlayer = this.gameManager.getGame(gameId).getActivePlayer();
		
		// If the player that sent this move is not the current active player, refuse to make turn
		if (!activePlayer.getUsername().equals(principal.getName())) {
			return new ResponseMessage("TurnFail", "User is not authorised to make a turn.", null);
		}
		
		Turn turn = new Turn(gameId, principal.getName());
		if (node.get("actions").isArray()) {
			for (JsonNode objNode : node.get("actions")) {
				String type = objNode.get("type").textValue();
				switch(type) {
					case "Move":
						Move move = objectMapper.readValue(objNode.toString(), Move.class);
						turn.addAction(move);
						break;
					case "Take":
						Take take = objectMapper.readValue(objNode.toString(), Take.class);
						turn.addAction(take);
						break;
					case "Drop":
						Drop drop = objectMapper.readValue(objNode.toString(), Drop.class);
						turn.addAction(drop);
						break;
					case "Promote":
						Promote promote = objectMapper.readValue(objNode.toString(), Promote.class);
						turn.addAction(promote);
						break;
					case "Winner":
						Winner winner = objectMapper.readValue(objNode.toString(), Winner.class);
						turn.addAction(winner);
						break;
					default:
						System.out.println("Unknown Action Type.");
						break;
				}
			}
		}
		this.gameManager.getGame(gameId).makeTurn(turn);//
		this.gameManager.toggleTurn(gameId);
		webSocket.convertAndSend("/topic/test", new ResponseMessage("TurnEmit", principal.getName() + " has made a turn.", turn)); // shows "made up username" from handshake
		return new ResponseMessage("TurnSuccess", "Successfully made turn.", null);
    }
	
	@GetMapping("/gameDetails")
	public ResponseMessage gameDetails(Principal principal) {
		List<GameDetail> gamesDetails = this.gameManager.getGameDetailsByUser(principal.getName());
		return new ResponseMessage("GameDetailsSuccess", "Successfully received game details.", gamesDetails);
	}
	
	@GetMapping("/gamesToJoin")
	public ResponseMessage gamesToJoin(Principal principal) {
		List<GameDetail> gamesDetails = this.gameManager.getGamesToJoin(principal.getName());
		return new ResponseMessage("GameDetailsSuccess", "Successfully received game details.", gamesDetails);
	}
	
	@PostMapping("/resignGame")
	public ResponseMessage resignGame(@RequestBody ResignData resignData) {
		int gameId = resignData.getGameId();
		String resigningPlayer = resignData.getResigningPlayer();
		if (this.gameManager.size() < gameId) {
			return new ResponseMessage("GameResignError", "Invalid game ID.", null);
		}
		Game game = this.gameManager.getGame(gameId);
		String winnerName = 
			game.getPlayer1().getUsername().equals(resigningPlayer) ? game.getPlayer2().getUsername() : game.getPlayer1().getUsername();
		Turn turn = new Turn(gameId, resigningPlayer);
		turn.addAction(new Winner(winnerName));
		game.makeTurn(turn);
		webSocket.convertAndSend("/topic/test", new ResponseMessage("TurnEmit", resigningPlayer + " has made a turn.", turn)); // shows "made up username" from handshake
		return new ResponseMessage("GameResignSuccess", "Successfully resigned from game.", null);
	}
	
	@GetMapping("/joinGame/{gameId}")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseMessage joinGame(Principal principal, @PathVariable int gameId) {
		Game game = this.gameManager.getGame(gameId);
		
		// if there is no empty player, then another player has joined
		if (!game.getPlayer1().getUsername().equals("") && !game.getPlayer2().getUsername().equals("")) {
			return new ResponseMessage("GameJoinFailure", "Failed to join game, no available player slot.");
		}
		
		if (game.getPlayer1().getUsername().equals("")) {
			game.getPlayer1().setUsername(principal.getName());
		} else if (game.getPlayer2().getUsername().equals("")) {
			game.getPlayer2().setUsername(principal.getName());
		}
		game.setStatus("Ongoing");
		return new ResponseMessage("GameJoinSuccess", "Successfully joined the game.");
	}
}
