package com.pokemon.ao.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@SuperBuilder
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PokemonVO implements ValueObject {
    protected Integer id;
    protected String name;
    protected SpeciesVO species;
    protected int currentHp;
    protected int maxHp;
    protected Map<MoveSlot, MoveVO> moves;
    private String originalTrainer;
}
