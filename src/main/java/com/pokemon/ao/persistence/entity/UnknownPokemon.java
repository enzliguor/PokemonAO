package com.pokemon.ao.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;


@EqualsAndHashCode(callSuper = true)
@Entity
@SuperBuilder
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@DiscriminatorValue(value = "unknown_pokemon")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UnknownPokemon extends Pokemon {

    @Column(name = "original_species_id")
    private Integer originalSpeciesID;

}
