package com.pokemon.ao.domain;

import lombok.*;
import java.util.Set;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PokemonVO implements ValueObject {
    private Integer id;
    private String name;
    private SpeciesVO species;
    private int currentHp;
    private int maxHp;
    private Set<MoveVO> moves;
    private String originalTrainer;
}
