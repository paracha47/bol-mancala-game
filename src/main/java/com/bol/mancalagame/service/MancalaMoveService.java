package com.bol.mancalagame.service;

import com.bol.mancalagame.model.MancalaGame;

public interface MancalaMoveService {
	
    MancalaGame move(String gameId, int pitIndex);

}
