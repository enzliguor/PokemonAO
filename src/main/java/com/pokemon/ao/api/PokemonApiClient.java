package com.pokemon.ao.api;

import com.pokemon.ao.domain.MoveVO;
import com.pokemon.ao.domain.PokemonVO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
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

    @Autowired
    private PokemonApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private static final String ERROR_MESSAGE = "Errore nella richiesta: {}";

    public PokemonVO getPokemon(Long pokemonID) {
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity("https://pokeapi.co/api/v2/pokemon/{id}", Map.class, pokemonID);
            if (response.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> jsonResponse = response.getBody();
                if (jsonResponse == null) return null;
                String name = (String) jsonResponse.get("name");
                int baseExperience = (int) jsonResponse.get("base_experience");
                String frontDefault = getFrontDefaultSprite((Map) jsonResponse.get("sprites"));
                Set<MoveVO> moves = getMoves((List) jsonResponse.get("moves"), 4);
                return new PokemonVO(pokemonID, name, frontDefault, baseExperience, baseExperience, null, moves, "Ash");
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

    public MoveVO getMoveByName(String moveName) {
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity("https://pokeapi.co/api/v2/move/{name}", Map.class, moveName);

            if (response.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> jsonResponse = response.getBody();
                if (jsonResponse == null) return null;

                Long id = (jsonResponse.get("id") != null) ? Long.valueOf(jsonResponse.get("id").toString()) : null;
                int power = (jsonResponse.get("power") != null) ? Integer.parseInt(jsonResponse.get("power").toString()) : 0;

                return new MoveVO(id, moveName, null, power);
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
