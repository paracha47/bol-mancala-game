package com.bol.mancalagame.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bol.mancalagame.model.MancalaGame;

public interface MancalaGameRepository extends MongoRepository<MancalaGame, String> {

}
