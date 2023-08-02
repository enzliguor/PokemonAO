package com.pokemon.ao.utility;

import com.pokemon.ao.api.PokemonApiClient;
import com.pokemon.ao.config.CustomProperties;
import com.pokemon.ao.domain.TypeVO;
import com.pokemon.ao.dto.MoveDTO;
import com.pokemon.ao.dto.SpeciesDTO;
import com.pokemon.ao.dto.converter.MoveConverterDTO;
import com.pokemon.ao.dto.converter.SpeciesConverterDTO;
import com.pokemon.ao.persistence.service.MoveService;
import com.pokemon.ao.persistence.service.PokemonService;
import com.pokemon.ao.persistence.service.SpeciesService;
import com.pokemon.ao.persistence.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
    private final JdbcTemplate jdbcTemplate;
    private final ResourceLoader resourceLoader;
    private final PokemonService pokemonService;
    @Value("${path.unknownDataTypeScript}")
    private String unknownDatatypeScriptPath;
    @Value("${path.insertPokemonDataScript}")
    private String insertPokemonDataScriptPath;

    @Autowired
    private DataLoader(PokemonApiClient pokemonApi, CustomProperties customProperties, SpeciesService speciesService, TypeService typeService, SpeciesConverterDTO speciesConverterDTO, MoveService moveService, MoveConverterDTO moveConverterDTO, JdbcTemplate jdbcTemplate, ResourceLoader resourceLoader, PokemonService pokemonService) {
        this.pokemonApi = pokemonApi;
        this.customProperties = customProperties;
        this.speciesService = speciesService;
        this.typeService = typeService;
        this.speciesConverterDTO = speciesConverterDTO;
        this.moveService = moveService;
        this.moveConverterDTO = moveConverterDTO;
        this.jdbcTemplate = jdbcTemplate;
        this.resourceLoader = resourceLoader;
        this.pokemonService = pokemonService;
    }

    @Override
    public void run(ApplicationArguments args) {
        loadTypes();
        loadSpecies();
        loadMoves();
        loadUnknownDataTypes();
        loadPokemon();}

    private void loadTypes() {
        if(!this.typeService.findAll().isEmpty()) return;
        Map<String, String> typeIcons = this.customProperties.getTypeIcons();
        typeIcons.forEach((key, value) -> this.typeService.save(TypeVO
                        .builder()
                        .name(key)
                        .icon(value)
                        .build()));
    }

    private void loadSpecies() {
        if(!this.speciesService.findAll().isEmpty()) return;
        int numberOfSpecies = this.customProperties.getSpeciesCount();
        Set<SpeciesDTO> species = this.pokemonApi.getSpecies(numberOfSpecies);
        species.stream()
                .map(this.speciesConverterDTO::convertFromDTOToVO)
                .forEach(this.speciesService::save);
    }

    private void loadMoves(){
        if(!this.moveService.findAll().isEmpty()) return;
        int numberOfMoves = this.customProperties.getMovesCount();
        List<TypeVO> types = this.typeService.findAll();
        Set<MoveDTO> moves = this.pokemonApi.getTypesMoves(types, numberOfMoves);
        moves.stream()
                .map(this.moveConverterDTO::convertFromDTOToVO)
                .forEach(this.moveService::save);
    }

    private void loadUnknownDataTypes() {
        if(containsUnknownDataTypes()){
            return;
        }
        this.executeScript(this.unknownDatatypeScriptPath);
    }
    private boolean containsUnknownDataTypes(){
        return this.typeService.exists(this.customProperties.getUnknownTypeID()) &&
                this.moveService.exists(this.customProperties.getUnknownMoveID()) &&
                this.speciesService.exists(this.customProperties.getUnknownSpeciesID());
    }

    private void loadPokemon(){
        if(this.customProperties.isStartWithEmptyPokemonTable() ||
        !this.pokemonService.findAll().isEmpty()) return;
        this.executeScript(this.insertPokemonDataScriptPath);
    }

    private void executeScript(String scriptPath){
        try {
            Resource resource = resourceLoader.getResource("file:" + scriptPath);
            String scriptContent = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()), StandardCharsets.UTF_8);

            String[] sqlBatch = scriptContent.split(";");

            jdbcTemplate.batchUpdate(sqlBatch);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
