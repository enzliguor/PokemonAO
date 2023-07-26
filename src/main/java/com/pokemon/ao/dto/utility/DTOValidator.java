package com.pokemon.ao.dto.utility;

import com.pokemon.ao.config.PropertyManager;
import com.pokemon.ao.dto.PokemonDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class DTOValidator {

    private final PropertyManager propertyManager;

    @Autowired
    public DTOValidator(PropertyManager propertyManager) {
        this.propertyManager = propertyManager;
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
                pokemonDTO.getMovesIds().size() > propertyManager.getMaxMovesPerPokemon() ||
                pokemonDTO.getMovesIds().size() < propertyManager.getMinMovesPerPokemon());
    }

}