package com.pokemon.ao.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PokemonVO {
    private Long id;
    private String name;
    private String sprite;
    private int currentHp;
    private int maxHp;
    private List<MoveVO> moves;
    private String originalTrainer;
}
