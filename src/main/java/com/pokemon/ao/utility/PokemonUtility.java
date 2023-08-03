package com.pokemon.ao.utility;

import com.pokemon.ao.config.CustomProperties;
import com.pokemon.ao.domain.PokemonVO;
import com.pokemon.ao.persistence.service.PokemonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

@Slf4j
@Component
public class PokemonUtility {
    private final PokemonService pokemonService;
    private final CustomProperties customProperties;

    @Autowired
    private PokemonUtility(PokemonService pokemonService, CustomProperties defaultPokemon) {
        this.pokemonService = pokemonService;
        this.customProperties = defaultPokemon;
    }

    public List<PokemonVO> getRandomPokemon(int pokemonToRetrieve) {
        List<PokemonVO> pokemonTeam = new ArrayList<>();
        List<Integer> idList = pokemonService.findAllIds();

        if (idList.isEmpty()) {
            return this.getDefaultTeam();
        }
        if (idList.size() <= pokemonToRetrieve) {
             return pokemonService.findAll();
        }
        Random random = null;
        try {
            random = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            log.error("Error getting random Pokemon", e);
            return this.getDefaultTeam();
        }
        while (pokemonTeam.size() < pokemonToRetrieve) {
            int n = random.nextInt(idList.size());
            PokemonVO pokemon = pokemonService.findById(idList.get(n));
            pokemonTeam.add(pokemon);
            idList.remove(n);
        }
        return pokemonTeam;
    }

    private List<PokemonVO> getDefaultTeam(){
        PokemonVO defaultPokemon = this.customProperties.getDefaultPokemon();
        PokemonVO savedPokemon = this.pokemonService.save(defaultPokemon);
        return List.of(savedPokemon);
    }
}
