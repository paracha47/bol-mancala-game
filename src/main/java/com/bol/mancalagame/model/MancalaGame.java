package com.bol.mancalagame.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.bol.mancalagame.constants.Constants;
import com.bol.mancalagame.exception.MancalaException;
import com.fasterxml.jackson.annotation.JsonInclude;



@Document
@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MancalaGame {
	
	@Id
	private String id;
    private List<MancalaPit> pits;
    private PlayerTurn playerTurn;
    private List<Player> players;
    private GameStatus status;
    private String gameWinner;
    private String comments;
    private MancalaPit targetPit;
    private int currentIndex;
    private GameWinner winner;

    public MancalaGame() {
        this (Constants.NUMBER_OF_PITS_PER_PLAYER);
	}

	public MancalaGame(Integer pitStones) {
		if(pitStones != null) {
			this.pits = new ArrayList<MancalaPit>();
			for (int i = 1; i <= Constants.defaultGameBoardSize; i++) {
				if (i % 7 == 0) {
					this.pits.add(new MancalaPit(i));
				} else {
					this.pits.add(new MancalaPit(i, pitStones));
				}
			}
		}
	}
	
	public MancalaGame(String id, Integer pitStones) {
        this(pitStones);
        this.id = id;
    }

    // returns the corresponding pit of particular index
    public MancalaPit getPit(Integer pitIndex) throws MancalaException {
        try {
            return this.pits.get(pitIndex-1);
        }catch (Exception e){
            throw  new MancalaException("Invalid pitIndex:"+ pitIndex +" has given!");
        }
    }

    @Override
    public String toString() {
        return "KalahaGame{" +
                ", pits=" + pits +
                ", playerTurn=" + playerTurn +
                '}';
    }
}
