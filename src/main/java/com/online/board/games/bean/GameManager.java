package com.online.board.games.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.online.board.games.model.Game;
import com.online.board.games.model.GameDetail;

@Component
public class GameManager {
	
	List<Game> games = new ArrayList<>();
	
	public Game getGame(int index) {
		return index >= this.size() ? null : this.games.get(index);
	}
	
	public void addGame(Game game) {
		game.setGameId(this.games.size());
		this.games.add(game);
	}

	public List<Game> getGames() {
		return games;
	}

	public void setGames(List<Game> games) {
		this.games = games;
	}
	
	public int size() {
		return this.games.size();
	}
	
	public void toggleTurn(int gameId) {
		this.games.get(gameId).toggleTurn();
	}
	
	public List<GameDetail> getGameDetailsByUser(String username) {
		List<GameDetail> gamesDetail = new ArrayList<GameDetail>();
		
		List<Game> usersGames = this.games.stream()
			.filter(game -> game.getPlayer1().getUsername().equals(username) || game.getPlayer2().getUsername().equals(username))
			.collect(Collectors.toList());
		
		usersGames.forEach(game -> {
			gamesDetail.add(mapGameToDetail(game));
		});
		
		return gamesDetail;
	}
	
	public List<GameDetail> getGamesToJoin(String username) {
		List<GameDetail> availableGamesDetail = new ArrayList<GameDetail>();
		
		// Filter games where the current user is NOT an active player, and where one of the players is empty
		List<Game> availableGames = this.games.stream()
			.filter(game -> !game.getPlayer1().getUsername().equals(username) && !game.getPlayer2().getUsername().equals(username) &&
				(game.getPlayer1().getUsername().equals("") || game.getPlayer2().getUsername().equals(""))
			)
			.collect(Collectors.toList());
		
		availableGames.forEach(game -> {
			availableGamesDetail.add(mapGameToDetail(game));
		});
		
		return availableGamesDetail;
	}
	
	
	public GameDetail getGameDetailsById(int gameId) {
		Game game = this.games.get(gameId);
		
		return mapGameToDetail(game);
	}
	
	//Convert single game to GameDetail
	private GameDetail mapGameToDetail(Game game) {
		return new GameDetail(
				game.getGameId(),
				game.getType(),
				game.getStatus(),
				game.getPlayer1(),
				game.getPlayer2()
		);
	}

}
