package com.bol.mancalagame.service;

import com.bol.mancalagame.constants.Constants;
import com.bol.mancalagame.model.BigPit;
import com.bol.mancalagame.model.GameStatus;
import com.bol.mancalagame.model.MancalaGame;
import com.bol.mancalagame.model.MancalaPit;
import com.bol.mancalagame.model.Player;
import com.bol.mancalagame.model.PlayerTurn;
import com.bol.mancalagame.model.GameWinner;
import com.bol.mancalagame.repository.MancalaGameRepository;
import com.bol.mancalagame.util.MancalaUtil;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MancalaMoveServiceImpl implements MancalaMoveService{

	private static final Logger logger = LoggerFactory.getLogger(MancalaMoveServiceImpl.class);

    @Autowired
    MancalaGameService gameService;

    @Autowired
    MancalaGameRepository gameRepository;
    
	@Override
	public MancalaGame move(String gameId, int pitIndex) {
		
		MancalaGame game = gameService.getGameById(gameId);

		logger.info("current turn : {}", game.getPlayerTurn());

		moveRight(game, game.getPlayers(), pitIndex);

		this.gameOverRule(game, game.getPlayers());
		MancalaUtil.switchPlayer(game);

		logger.info("next turn : {}", game.getPlayerTurn());
		return gameRepository.save(game);
	}
	
	private void moveRight(MancalaGame game, List<Player> players, int pitIndex) {
		game.setStatus(GameStatus.CURRENT_PLAYER);

		PlayerTurn playerTurn = game.getPlayerTurn();
		List<MancalaPit> currentBoard = players.get(playerTurn.getTurn()).getPits();
		BigPit currentBigPit = players.get(playerTurn.getTurn()).getBigPit();

		int numberOfStonesToMove = currentBoard.get(pitIndex - 1).clear();

		while (numberOfStonesToMove > 0) {
			if (pitIndex < Constants.NUMBER_OF_PITS_PER_PLAYER) {
				numberOfStonesToMove = currentBoard.get(pitIndex).move(numberOfStonesToMove);
				this.lastStoneInEmptyPitRule(game, numberOfStonesToMove, pitIndex);
				pitIndex++;
			} else {
				numberOfStonesToMove = moveCurrentPlayerBigPit(game, currentBigPit, numberOfStonesToMove);

				if (numberOfStonesToMove == 0)
					game.setStatus(GameStatus.BIG_PIT);
				else
					currentBoard = MancalaUtil.switchBoard(game, players);

				pitIndex = 0;
			}
		}
	}

	private void lastStoneInEmptyPitRule(MancalaGame game, int numberOfStonesToMove, int pitIndex) {
		if (numberOfStonesToMove != 0)
			return;

		List<Player> players = game.getPlayers();
		List<MancalaPit> currentBoard = players.get(game.getPlayerTurn().getTurn()).getPits();
		List<MancalaPit> opponentBoard = players.get(game.getPlayerTurn().getTurn() ^ 1).getPits();
		BigPit currentBigPit = players.get(game.getPlayerTurn().getTurn()).getBigPit();
		int opponentPitIndex = opponentBoard.size() - pitIndex - 1;

		if (GameStatus.CURRENT_PLAYER.equals(game.getStatus()) && currentBoard.get(pitIndex).getStones() == 1
				&& !opponentBoard.get(opponentPitIndex).isEmpty()) {
			int currentPitStones = currentBoard.get(pitIndex).clear();
			int opponentPitStones = opponentBoard.get(opponentPitIndex).clear();
			currentBigPit.addStones(currentPitStones + opponentPitStones);
		}
	}
	
	private void gameOverRule(MancalaGame game, List<Player> players) {
        int currentPlayerStones = MancalaUtil.getTotalStonesInPlayerPit(players, game.getPlayerTurn().getTurn());

        if (currentPlayerStones != 0)
            return;

        int otherPlayerStones = MancalaUtil.getTotalStonesInPlayerPit(players, game.getPlayerTurn().getTurn() ^ 1);

        players.get(game.getPlayerTurn().getTurn()).getBigPit().addStones(currentPlayerStones);
        players.get(game.getPlayerTurn().getTurn() ^ 1).getBigPit().addStones(otherPlayerStones);

        MancalaUtil.resetPitsToZero(game.getPlayerTurn().getTurn() ^ 1, players);

        Optional<Player> winningPlayer = players.stream()
                .max(Comparator.comparing(player -> player.getBigPit().getStones()));

        if (winningPlayer.isPresent()) {
            GameWinner winner = new GameWinner(players.get(game.getPlayerTurn().getTurn()).getName(),
                    winningPlayer.get().getBigPit().getStones());
            game.setWinner(winner);
            game.setStatus(GameStatus.GAME_OVER);
        }
    }
	
	private int moveCurrentPlayerBigPit(MancalaGame game, BigPit currentBigPit, int numberOfStonesToMove) {
        return GameStatus.CURRENT_PLAYER.equals(game.getStatus()) ?
                currentBigPit.move(numberOfStonesToMove) : numberOfStonesToMove;
    }
	
}
