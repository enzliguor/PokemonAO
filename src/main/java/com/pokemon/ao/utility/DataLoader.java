package com.pokemon.ao.utility;

import com.pokemon.ao.api.PokemonApiClient;
import com.pokemon.ao.config.CustomProperties;
import com.pokemon.ao.domain.TypeVO;
import com.pokemon.ao.dto.MoveDTO;
import com.pokemon.ao.dto.SpeciesDTO;
import com.pokemon.ao.dto.converter.MoveConverterDTO;
import com.pokemon.ao.dto.converter.SpeciesConverterDTO;
import com.pokemon.ao.persistence.service.MoveService;
import com.pokemon.ao.persistence.service.SpeciesService;
import com.pokemon.ao.persistence.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class DataLoader implements ApplicationRunner {

    private final PokemonApiClient pokemonApi;
    private final CustomProperties customProperties;
    private final SpeciesService speciesService;
    private final TypeService typeService;
    private final SpeciesConverterDTO speciesConverterDTO;
    private final MoveService moveService;
    private final MoveConverterDTO moveConverterDTO;

    @Autowired
    public DataLoader(PokemonApiClient pokemonApi, CustomProperties customProperties, SpeciesService speciesService, TypeService typeService, SpeciesConverterDTO speciesConverterDTO, MoveService moveService, MoveConverterDTO moveConverterDTO) {
        this.pokemonApi = pokemonApi;
        this.customProperties = customProperties;
        this.speciesService = speciesService;
        this.typeService = typeService;
        this.speciesConverterDTO = speciesConverterDTO;
        this.moveService = moveService;
        this.moveConverterDTO = moveConverterDTO;
    }

    @Override
    public void run(ApplicationArguments args) {
        loadTypes();
        loadSpecies();
        loadMoves();
    }

    public void loadTypes() {
        if(!this.typeService.findAll().isEmpty()) return;
        Map<String, String> typeIcons = this.customProperties.getTypeIcons();
        typeIcons.forEach((key, value) -> this.typeService.save(TypeVO
                        .builder()
                        .name(key)
                        .icon(value)
                        .build()));
    }

    public void loadSpecies() {
        if(!this.speciesService.findAll().isEmpty()) return;
        int numberOfSpecies = this.customProperties.getSpeciesCount();
        Set<SpeciesDTO> species = this.pokemonApi.getSpecies(numberOfSpecies);
        species.stream()
                .map(this.speciesConverterDTO::convertFromDTOToVO)
                .forEach(this.speciesService::save);
    }

    public void loadMoves(){
        if(!this.moveService.findAll().isEmpty()) return;
        int numberOfMoves = this.customProperties.getMovesCount();
        List<TypeVO> types = this.typeService.findAll();
        Set<MoveDTO> moves = this.pokemonApi.getTypesMoves(types, numberOfMoves);
        moves.stream()
                .map(this.moveConverterDTO::convertFromDTOToVO)
                .forEach(this.moveService::save);
    }
}
