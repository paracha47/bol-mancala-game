package com.bol.mancalagame.controller;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bol.mancalagame.model.MancalaGame;
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
	
	@PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<MancalaGame> createGame(@RequestParam @NotNull String firstPlayerName,
                                                  @RequestParam @NotNull String secondPlayerName) {
        MancalaGame game = mancalaGameService.createGame(firstPlayerName, secondPlayerName);
        MancalaUtil.printBoard(game);
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

