package com.pokemon.ao.domain;

import lombok.*;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PokemonVO implements ValueObject{
    private Long id;
    private String name;
    private SpeciesVO speciesVO;
    private int currentHp;
    private int maxHp;
    private Set<MoveVO> moves;
    private String originalTrainer;
}
