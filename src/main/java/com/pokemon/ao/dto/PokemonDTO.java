package com.pokemon.ao.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import java.util.Set;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class PokemonDTO implements DTO{

    private Integer speciesId;

    private String name;

    private int currentHp;

    private int maxHp;

    private Integer typeId;

    private Set<Integer> movesIds;

    private String originalTrainer;
}
