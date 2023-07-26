package com.pokemon.ao.utility;

import com.pokemon.ao.config.CustomProperties;
import com.pokemon.ao.domain.PokemonVO;
import com.pokemon.ao.persistence.service.PokemonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Component
public class PokemonUtility {
    private final PokemonService pokemonService;
    private final CustomProperties customProperties;

    @Autowired
    public PokemonUtility(PokemonService pokemonService, CustomProperties defaultPokemon) {
        this.pokemonService = pokemonService;
        this.customProperties = defaultPokemon;
    }

    public List<PokemonVO> getRandomPokemons(int pokemonToRetrieve) {
        List<PokemonVO> pokemonList = new ArrayList<>();
        List<Integer> idList = pokemonService.findAllIds();
        try {
            if (pokemonToRetrieve < idList.size()) {
                Random rand = SecureRandom.getInstanceStrong();
                while (pokemonList.size() < pokemonToRetrieve) {
                    int n = rand.nextInt(idList.size());
                    PokemonVO pokemon = pokemonService.findById(idList.get(n));
                    pokemonList.add(pokemon);
                    idList.remove(n);
                }
            }else if(!idList.isEmpty()){
                pokemonList = pokemonService.findAll();
            }
            if(pokemonList.isEmpty()) {
                pokemonList.add(customProperties.getDefaultPokemon());
            }
        }catch(NoSuchAlgorithmException e) {
            log.error("Error getting random Pokemon {}", e.getMessage ());
            pokemonList.add(customProperties.getDefaultPokemon());
        }
        return pokemonList;
    }
}
