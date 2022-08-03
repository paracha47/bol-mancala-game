package com.bol.mancalagame.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bol.mancalagame.constants.Constants;
import com.bol.mancalagame.model.GameStatus;
import com.bol.mancalagame.model.MancalaGame;
import com.bol.mancalagame.model.MancalaPit;
import com.bol.mancalagame.model.Player;
import com.bol.mancalagame.model.PlayerTurn;

public class MancalaUtil {

    private static final Logger logger = LoggerFactory.getLogger(MancalaUtil.class);

	 public static void printBoard(MancalaGame game) {
	        StringBuilder stringBuilder = new StringBuilder();
	        for (int i = 0; i < 6; i++) {
	            stringBuilder.append(game.getPlayers().get(0).getPits().get(i).getStones())
	                    .append(" ");
	        }
	        stringBuilder.append(" | ")
	                .append(game.getPlayers().get(0).getBigPit().getStones());
	        logger.debug("Player 1 : {}", stringBuilder);

	        stringBuilder = new StringBuilder();

	        for (int i = 0; i < 6; i++) {
	            stringBuilder.append(game.getPlayers().get(1).getPits().get(i).getStones())
	                    .append(" ");
	        }
	        stringBuilder.append(" | ")
	                .append(game.getPlayers().get(1).getBigPit().getStones());
	        logger.debug("Player 2 : {}", stringBuilder);
	    }

	    public static void switchPlayer(MancalaGame game) {
	        PlayerTurn playerTurn = game.getPlayerTurn();

	        if (!GameStatus.GAME_OVER.equals(game.getStatus()) && !GameStatus.BIG_PIT.equals(game.getStatus()))
	            game.setPlayerTurn(
	                    playerTurn.equals(PlayerTurn.PLAYER_A) ? PlayerTurn.PLAYER_B : PlayerTurn.PLAYER_A);
	    }

	    public static List<MancalaPit> switchBoard(MancalaGame game, List<Player> players) {
	        game.setStatus(GameStatus.CURRENT_PLAYER.equals(game.getStatus()) ?
	                GameStatus.OTHER_PLAYER : GameStatus.CURRENT_PLAYER);
	        int nextBoard = (GameStatus.CURRENT_PLAYER.equals(game.getStatus())) ?
	                game.getPlayerTurn().getTurn() : game.getPlayerTurn().getTurn() ^ 1;
	        return players.get(nextBoard).getPits();
	    }

	    public static int getTotalStonesInPlayerPit(List<Player> players, int i) {
	        return players.get(i)
	                .getPits()
	                .stream()
	                .mapToInt(MancalaPit::getStones)
	                .sum();
	    }

	    public static void resetPitsToZero(int turn, List<Player> players) {
	        players.get(turn).setPits(
	                IntStream.rangeClosed(0, Constants.NUMBER_OF_PITS_PER_PLAYER)
	                        .filter(i -> i < Constants.NUMBER_OF_PITS_PER_PLAYER)
	                        .mapToObj(i -> new MancalaPit(i, 0))
	                        .collect(Collectors.toList())
	        );
	    }
}
