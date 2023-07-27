package com.pokemon.ao.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.util.Set;

@Builder
@Getter
@EqualsAndHashCode
public class PokemonDTO implements DTO {

    private Integer speciesId;

    private String name;

    private int currentHp;

    private int maxHp;

    private Integer typeId;

    private Set<Integer> movesIds;

    private String originalTrainer;


    @JsonCreator
    private PokemonDTO(@JsonProperty("id") Integer speciesId,
                      @JsonProperty("name") String name,
                      @JsonProperty("current_hp") int currentHp,
                      @JsonProperty("max_hp") int maxHp,
                      @JsonProperty("type") Integer typeId,
                      @JsonProperty("moves") Set<Integer> movesIds,
                      @JsonProperty("original_trainer") String originalTrainer) {
        this.speciesId = speciesId;
        this.name = name;
        this.currentHp = currentHp;
        this.maxHp = maxHp;
        this.typeId = typeId;
        this.movesIds = movesIds;
        this.originalTrainer = originalTrainer;
    }
}
