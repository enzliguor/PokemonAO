package com.pokemon.ao.utility;

import com.pokemon.ao.api.PokemonApiClient;
import com.pokemon.ao.config.PropertyManager;
import com.pokemon.ao.domain.TypeVO;
import com.pokemon.ao.dto.SpeciesDTO;
import com.pokemon.ao.dto.converter.SpeciesConverterDTO;
import com.pokemon.ao.persistence.service.SpeciesService;
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
    private final SpeciesService speciesService;
    private final TypeService typeService;
    private final SpeciesConverterDTO speciesConverterDTO;

    @Autowired
    public DataLoader(PokemonApiClient pokemonApi, PropertyManager propertyManager, SpeciesService speciesService, TypeService typeService, SpeciesConverterDTO speciesConverterDTO) {
        this.pokemonApi = pokemonApi;
        this.propertyManager = propertyManager;
        this.speciesService = speciesService;
        this.typeService = typeService;
        this.speciesConverterDTO = speciesConverterDTO;
    }

    @Override
    public void run(ApplicationArguments args) {
        loadTypes();
        loadSpecies();
    }

    public void loadTypes() {
        if(!this.typeService.findAll().isEmpty()) return;
        Set<String> typeNames = propertyManager.getTypeNames();
        for (String typeName : typeNames) {
            String typeIcon = propertyManager.getIcon(typeName);
            typeService.save(TypeVO.builder()
                    .name(typeName)
                    .icon(typeIcon)
                    .build());
        }
    }

    public void loadSpecies() {
        if(!this.speciesService.findAll().isEmpty()) return;
        int numberOfSpecies = this.propertyManager.getSpeciesCount();
        Set<SpeciesDTO> species = this.pokemonApi.getSpecies(numberOfSpecies);
        species.stream()
                .map(this.speciesConverterDTO::convertFromDTOToVO)
                .forEach(this.speciesService::save);
    }

}
