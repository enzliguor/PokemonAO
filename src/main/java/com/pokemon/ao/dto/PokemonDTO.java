package com.pokemon.ao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.util.Set;

@Builder
@Getter
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PokemonDTO implements DTO {

    @JsonProperty("id")
    private Integer speciesId;

    private String name;

    @JsonProperty("current_hp")
    private int currentHp;

    @JsonProperty("max_hp")
    private int maxHp;

    @JsonProperty("type")
    private Integer typeId;

    @JsonProperty("moves")
    private Set<Integer> movesIds;

    @JsonProperty("original_trainer")
    private String originalTrainer;
}
