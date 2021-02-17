package com.online.board.games.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import com.online.board.games.bean.GameManager;
import com.online.board.games.model.Game;
import com.online.board.games.model.ResponseMessage;


@Controller
public class GameWebsocketController {
	
	@Autowired
	private GameManager gameManager;
	
	@Autowired
	private SimpMessagingTemplate webSocket;
	
	@MessageMapping("/fetchGame/{gameId}")
    @SendTo("/topic")
    public ResponseMessage fetchGame(Principal principal, @RequestParam(name = "gameId") String gameId) throws Exception {
		if (!gameId.matches("[0-9]+")) {
			return new ResponseMessage("FetchGameError", "Invalid ID.");
		}
		int id = Integer.parseInt(gameId);
		if (this.gameManager.size() <= id) {
			return new ResponseMessage("FetchGameError", "Game with that ID does not exist.");
		}
		Game game = this.gameManager.getGame(id);
		return new ResponseMessage("FetchGameSuccess", "Retrieved game with id=" + id, game);
    }
	
	@MessageMapping("/test")
    public void test(@Payload String message, Principal principal) throws Exception {
		this.webSocket.convertAndSend("/topic/test", new ResponseMessage("TestSuccess", "Thanks for the request " + principal.getName())); // shows "placeholder" from handshake
    }
	


}
