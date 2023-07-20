package com.pokemon.ao.utility;

import com.pokemon.ao.api.PokemonApiClient;
import com.pokemon.ao.config.PropertyManager;
import com.pokemon.ao.domain.PokemonVO;
import com.pokemon.ao.domain.TypeVO;
import com.pokemon.ao.persistence.service.PokemonService;
import com.pokemon.ao.persistence.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataLoader implements ApplicationRunner {

    private final PokemonApiClient pokemonApi;
    private final PropertyManager propertyManager;
    private final PokemonService pokemonService;
    private final TypeService typeService;
    @Autowired
    public DataLoader(PokemonApiClient pokemonApi, PropertyManager propertyManager, PokemonService pokemonService, TypeService typeService) {
        this.pokemonApi = pokemonApi;
        this.propertyManager = propertyManager;
        this.pokemonService = pokemonService;
        this.typeService = typeService;
    }

    @Override
    public void run(ApplicationArguments args) {
        loadTypes();
        int limit = 50;
        for (long i = 0; i < limit; i++) {
            PokemonVO pokemonVO = this.pokemonApi.getPokemon(i);
            this.pokemonService.save(pokemonVO);
        }
    }

    public void loadTypes() {
        Set<String> typeNames = propertyManager.getTypeNames();
        for (String typeName: typeNames) {
            String typeIcon = propertyManager.getIcon(typeName);
            typeService.save(new TypeVO(null, typeName, typeIcon));
        }
    }
}
