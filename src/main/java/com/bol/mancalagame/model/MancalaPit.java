package com.bol.mancalagame.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MancalaPit implements Serializable{

	private Integer id;
	private Integer stones;

	@JsonIgnore
	public Boolean isEmpty() {
		return this.stones == 0;
	}

	public int clear() {
		int currentPitStones = this.stones;
        this.stones = 0;
        return currentPitStones;	
     }

	public void move() {
		this.stones++;
	}

	public int move(int numberOfStonesToMove) {
        this.stones ++;
        return --numberOfStonesToMove;
    }
	
	public void addStones(Integer stones) {
		this.stones += stones;
	}

	public MancalaPit(Integer id) {
		this.id = id;
	}

}
