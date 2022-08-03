package com.bol.mancalagame.model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.*;

@Document
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class GameWinner {
    private String playerName;
    private int score;
}