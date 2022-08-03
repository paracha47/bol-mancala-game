package com.bol.mancalagame.controller;

import java.util.Iterator;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.bol.mancalagame.model.MancalaGame;
import com.bol.mancalagame.model.MancalaPit;
import com.bol.mancalagame.service.MancalaGameService;
import com.bol.mancalagame.service.MancalaMoveService;
import com.bol.mancalagame.util.MancalaUtil;

@RestController
@CrossOrigin
@Validated
@RequestMapping("/mancala")
public class MancalaRestController {

	private static final Logger logger = LoggerFactory.getLogger(MancalaRestController.class);

	private MancalaGameService mancalaGameService;

	private MancalaMoveService mancalaService;

	@Autowired
    public MancalaRestController (MancalaGameService mancalaGameService, MancalaMoveService mancalaMoveService){
        this.mancalaGameService = mancalaGameService;
        this.mancalaService = mancalaMoveService;
    }
	
//	@Value("${mancala.pit.stones}")
//    private Integer pitStones;
	
	 @PostMapping("/create")
	    @ResponseStatus(HttpStatus.CREATED)
	    public ResponseEntity<MancalaGame> createGame(@RequestParam @NotNull String firstPlayerName,
	                                                  @RequestParam @NotNull String secondPlayerName) {
	        MancalaGame game = mancalaGameService.createGame(firstPlayerName, secondPlayerName);
	        MancalaUtil.printBoard(game);
//	        Iterator<MancalaPit> pi = Iterables.cycle(game.getPits()).iterator();
//	        for (int i = 0; i < 100; i++) {
//	            MancalaPit pit = pi.next();
//	            System.out.println(pit.getId()+" : "+pit.getStones());
//	        }
	        logger.info("mancala game created : {}", game);
	        return new ResponseEntity<>(game, HttpStatus.CREATED);
	    }

	    @GetMapping("/{gameId}")
	    public ResponseEntity<MancalaGame> getGame(@PathVariable String gameId) {
	        MancalaGame game = mancalaGameService.getGameById(gameId);
	        if(game == null)
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        return new ResponseEntity<>(game, HttpStatus.OK);
	    }

	    @PostMapping("/{gameId}/sow")
	    public ResponseEntity<MancalaGame> sow(@PathVariable String gameId,
	                                               @RequestParam @NotNull @Min(1) @Max(6) int pitIndex) {
	        MancalaGame game = mancalaService.move(gameId, pitIndex);
	        return new ResponseEntity<>(game, HttpStatus.OK);
	    }

}

