package com.pokemon.ao.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.Set;

@Getter
@AllArgsConstructor
public class PokemonVO implements ValueObject{
    private Long id;
    private String name;
    private String sprite;
    private int currentHp;
    private int maxHp;
    private TypeVO type;
    private Set<MoveVO> moves;
    private String originalTrainer;
}
