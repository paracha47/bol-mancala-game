package com.bol.mancalagame.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.bol.mancalagame.constants.Constants;
import com.bol.mancalagame.exception.MancalaException;
import com.bol.mancalagame.model.BigPit;
import com.bol.mancalagame.model.GameStatus;
import com.bol.mancalagame.model.MancalaGame;
import com.bol.mancalagame.model.MancalaPit;
import com.bol.mancalagame.model.Player;
import com.bol.mancalagame.repository.MancalaGameRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MancalaGameServiceImpl implements MancalaGameService{

    private static final Logger logger = LoggerFactory.getLogger(MancalaGameServiceImpl.class);

    @Value("${mancala.game.number.of.stones.per.pit}")
    private int numberOfStonesPerPit;
    
    private final MancalaGameRepository mancalaGameRepository;

    @Autowired
    public MancalaGameServiceImpl(MancalaGameRepository mancalaGameRepository){
        this.mancalaGameRepository = mancalaGameRepository;
    }
    
	@Override
	public MancalaGame createGame(String firstPlayer, String secondPlayer) {
		
		logger.info("----- start creating mancala game-----");
	    List<Player> players = Arrays.asList(
                this.createPlayer(firstPlayer),
                this.createPlayer(secondPlayer));
	    List<MancalaPit> pits = new MancalaGame(Constants.NUMBER_OF_PITS_PER_PLAYER).getPits();
		MancalaGame mancalaGame = MancalaGame.builder()
					                .pits(pits)
					                .players(players)
					                .status(GameStatus.IN_PROGRESS)
					                .build();
		
        mancalaGameRepository.save(mancalaGame);
        
		logger.info("---- Game created successfully -> ()",mancalaGame);

        return mancalaGame;
	}
	
	private Player createPlayer(String playerName) {
        logger.info("create mancala player : {}", playerName);

        return Player.builder()
                .name(playerName)
                .pits(IntStream.rangeClosed(0, Constants.NUMBER_OF_PITS_PER_PLAYER)
                        .filter(i -> i < Constants.NUMBER_OF_PITS_PER_PLAYER)
                        .mapToObj(i -> new MancalaPit(i, numberOfStonesPerPit))
                        .collect(Collectors.toList()))
                .bigPit(new BigPit(Constants.NUMBER_OF_PITS_PER_PLAYER))
                .build();
    }

	@Override
	public MancalaGame saveGame(MancalaGame game) {
		logger.info("----- save game -> () -----");
        return mancalaGameRepository.save(game);
	}

	@Override
	public List<MancalaGame> getAllGames() {
		logger.info("----- get All games -> ()-----");
        return mancalaGameRepository.findAll();
	}

	@Override
	public MancalaGame loadGame(String gameId) {
		logger.info("----- Load Game details -> () ",gameId);
        Optional<MancalaGame> optional = mancalaGameRepository.findById(gameId);
        return optional.orElseThrow(
        		() -> new MancalaException("Game Not Found"));
	}

	@Override
	public MancalaGame getGameById(String gameId) {
		Optional<MancalaGame> mancalaGameOptional = mancalaGameRepository.findById(gameId);
        if (mancalaGameOptional.isEmpty())
            throw new MancalaException("Game Not Found =>() " + gameId);

        if (mancalaGameOptional.get().getPlayers() == null || mancalaGameOptional.get().getPlayers().size() != 2 ||
                mancalaGameOptional.get().getPlayerTurn() == null)
            throw new MancalaException("Game Is Invalid =>() " + gameId);
        
        return mancalaGameOptional.get();
	}
	
}
