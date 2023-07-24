package com.pokemon.ao.api;

import com.pokemon.ao.config.PropertyManager;
import com.pokemon.ao.domain.MoveVO;
import com.pokemon.ao.domain.PokemonVO;
import com.pokemon.ao.domain.SpeciesVO;
import com.pokemon.ao.domain.TypeVO;
import com.pokemon.ao.persistence.service.MoveService;
import com.pokemon.ao.persistence.service.TypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class PokemonApiClient {
    private final RestTemplate restTemplate;
    private final TypeService typeService;
    private final MoveService moveService;
    private final PropertyManager propertyManager;

    @Autowired
    private PokemonApiClient(RestTemplate restTemplate, TypeService typeService, MoveService moveService, PropertyManager propertyManager) {
        this.restTemplate = restTemplate;
        this.typeService = typeService;
        this.moveService = moveService;
        this.propertyManager = propertyManager;
    }

    private static final String ERROR_MESSAGE = "Errore nella richiesta: {}";

    public PokemonVO getPokemon(Integer speciesID) {
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(propertyManager.getPokeApiUrl(), Map.class, speciesID);
            if (response.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> jsonResponse = response.getBody();
                if (jsonResponse == null) return null;
                String name = (String) jsonResponse.get("name");
                int baseExperience = (int) jsonResponse.get("base_experience");
                String typeName = getTypeName((List) jsonResponse.get("types"));
                TypeVO type = typeService.findByName(typeName);
                String frontDefault = getFrontDefaultSprite((Map) jsonResponse.get("sprites"));
                SpeciesVO species = new SpeciesVO(speciesID, frontDefault, name, type);
                Set<MoveVO> moves = getMoves((List) jsonResponse.get("moves"), 4);
                return new PokemonVO(null, "nickName", species, baseExperience, baseExperience, moves, "Ash");
            } else {
                log.error(ERROR_MESSAGE, response.getStatusCode());
                return null;
            }
        } catch (Exception e) {
            log.error(ERROR_MESSAGE, e.getMessage());
            return null;
        }
    }

    private String getFrontDefaultSprite(Map<String, Map<String, Map<String, String>>> sprites) {
        if (sprites == null) return null;
        return sprites.get("other")
                .get("home")
                .get("front_default");
    }

    private Set<MoveVO> getMoves(List<Map<String, Object>> moves, int movesNumber) {
        return moves.stream()
                .limit(movesNumber)
                .map(moveObject -> (Map<String, String>) moveObject.get("move"))
                .map(moveMap -> moveMap.get("name"))
                .map(this::getMoveByName)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private String getTypeName(List<Map<String, Object>> types) {
        if (types == null || types.isEmpty()){
            return null;
        }
        Map<String, Object> typeObj = types.get(0);
        Map<String, Object> typeMap = (Map<String, Object>) typeObj.get("type");
        return (String) typeMap.get("name");
    }

    public MoveVO getMoveByName(String moveName) {
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity("https://pokeapi.co/api/v2/move/{name}", Map.class, moveName);

            if (response.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> jsonResponse = response.getBody();
                if (jsonResponse == null) return null;

                Integer id = (jsonResponse.get("id") != null) ? Integer.valueOf(jsonResponse.get("id").toString()) : null;
                int power = (jsonResponse.get("power") != null) ? Integer.parseInt(jsonResponse.get("power").toString()) : 0;
                Map<String, Object> typeObj = (Map<String, Object>) jsonResponse.get("type");
                String typeName = typeObj != null ? (String) typeObj.get("name") : null;
                TypeVO moveType = typeService.findByName(typeName);
                MoveVO move = new MoveVO(id, moveName, moveType, power);
                moveService.save(move);
                return move;
            } else {
                log.error(ERROR_MESSAGE, response.getStatusCode());
                return null;
            }
        } catch (RestClientException e) {
            log.error(ERROR_MESSAGE, e.getMessage());
            return null;
        }
    }
}
