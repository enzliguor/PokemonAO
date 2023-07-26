package com.pokemon.ao.dto.utility;

import com.pokemon.ao.config.CustomProperties;
import com.pokemon.ao.dto.PokemonDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class DTOValidator {

    private final CustomProperties customProperties;

    @Autowired
    public DTOValidator(CustomProperties customProperties) {
        this.customProperties = customProperties;
    }


    public boolean isValidPokemonDTO(PokemonDTO pokemonDTO) {
        return !(pokemonDTO == null ||
                pokemonDTO.getSpeciesId() == null ||
                pokemonDTO.getName() == null ||
                pokemonDTO.getCurrentHp() < 0 ||
                pokemonDTO.getMaxHp() <= 0 ||
                pokemonDTO.getTypeId() == null ||
                pokemonDTO.getMovesIds() == null ||
                pokemonDTO.getOriginalTrainer() == null ||
                pokemonDTO.getMovesIds().isEmpty() ||
                pokemonDTO.getMovesIds().size() > customProperties.getMaxMovesPerPokemon() ||
                pokemonDTO.getMovesIds().size() < customProperties.getMinMovesPerPokemon());
    }

}