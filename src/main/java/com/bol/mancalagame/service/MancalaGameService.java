package com.bol.mancalagame.service;

import java.util.List;

import com.bol.mancalagame.model.MancalaGame;

public interface MancalaGameService {

	MancalaGame createGame(String firstPlayer, String secondPlayer);
	MancalaGame saveGame(MancalaGame game);
    List<MancalaGame> getAllGames();
    MancalaGame getGameById(String gameId);
    MancalaGame loadGame(String gameId);
}
