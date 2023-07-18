package com.pokemon.ao.utility;

import com.pokemon.ao.domain.PokemonVO;
import com.pokemon.ao.persistence.entity.Pokemon;
import com.pokemon.ao.persistence.service.PokemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class PokemonUtility {
    private final PokemonService pokemonService;

    @Autowired
    public PokemonUtility(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    public List<PokemonVO> getRandomPokemons(int pokemonToRetrieve, int seed) {
        List<PokemonVO> pokemonList = new ArrayList<>();
        List<Long> idList = pokemonService.findAllIds();

        if (pokemonToRetrieve <= idList.size()) {
            Random rand = new Random(seed);

            while (pokemonList.size() < pokemonToRetrieve) {
                int n = rand.nextInt(0, idList.size());

                PokemonVO pokemon = pokemonService.findById(idList.get(n));

                pokemonList.add(pokemon);
                idList.remove(n);
            }
        } else {
            // TODO Creare il pokemon mockato da property e inserirlo nella lista
        }

        return pokemonList;
    }
}
