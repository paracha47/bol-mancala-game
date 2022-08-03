package com.bol.mancalagame.model;

import lombok.*;

@AllArgsConstructor
@Getter
@ToString
public enum PlayerTurn {
	
	PLAYER_A (0),
    PLAYER_B (1);

    private int turn;

}
