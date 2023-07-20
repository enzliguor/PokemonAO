package com.pokemon.ao.utility;

import com.pokemon.ao.api.PokemonApiClient;
import com.pokemon.ao.domain.PokemonVO;
import com.pokemon.ao.persistence.service.PokemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {

    private final PokemonApiClient pokemonApi;

    private final PokemonService pokemonService;
    @Autowired
    public DataLoader(PokemonApiClient pokemonApi, PokemonService pokemonService) {
        this.pokemonApi = pokemonApi;
        this.pokemonService = pokemonService;
    }

    @Override
    public void run(ApplicationArguments args) {
        int limit = 50;
        for (long i = 0; i < limit; i++) {
            PokemonVO pokemonVO = this.pokemonApi.getPokemon(i);
            this.pokemonService.save(pokemonVO);
        }
    }
}
