package com.bol.mancalagame.model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.*;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Player {

	private String name;
	private List<MancalaPit> pits;
    private BigPit bigPit;
    
    public Player(String name) {
    	this.name = name;
	}
}
