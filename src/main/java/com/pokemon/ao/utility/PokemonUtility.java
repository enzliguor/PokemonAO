package com.pokemon.ao.utility;

import com.pokemon.ao.config.PropertyManager;
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
    private final PropertyManager propertyManager;

    @Autowired
    public PokemonUtility(PokemonService pokemonService, PropertyManager defaultPokemon) {
        this.pokemonService = pokemonService;
        this.propertyManager = defaultPokemon;
    }

    public List<PokemonVO> getRandomPokemons(int pokemonToRetrieve) throws NoSuchAlgorithmException {
        List<PokemonVO> pokemonList = new ArrayList<>();
        List<Long> idList = pokemonService.findAllIds();

        if (pokemonToRetrieve < idList.size()) {
            Random rand = SecureRandom.getInstanceStrong();

            while (pokemonList.size() < pokemonToRetrieve) {
                int n = rand.nextInt(0, idList.size());

                PokemonVO pokemon = pokemonService.findById(idList.get(n));

                pokemonList.add(pokemon);
                idList.remove(n);
            }
        } else if(!idList.isEmpty()){
            pokemonList = pokemonService.findAll();
        }else{
            pokemonList.add(propertyManager.getDefaultPokemon());
        }
        return pokemonList;
    }
}
