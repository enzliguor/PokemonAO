package com.pokemon.ao.dto;

import lombok.*;
import java.util.Set;

@Builder
@Getter
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PokemonDTO implements DTO {

    private Integer speciesId;

    private String name;

    private int currentHp;

    private int maxHp;

    private Integer typeId;

    private Set<Integer> movesIds;

    private String originalTrainer;
}
