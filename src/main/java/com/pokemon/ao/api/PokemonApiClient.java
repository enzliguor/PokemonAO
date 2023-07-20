package com.pokemon.ao.api;

import com.pokemon.ao.domain.MoveVO;
import com.pokemon.ao.domain.PokemonVO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PokemonApiClient {
    private final RestTemplate restTemplate = new RestTemplate();
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
                Set<MoveVO> moves = getMoves(name, 4);
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

    public Set<MoveVO> getMoves(String pokemonName, int movesNumber) {
        Set<MoveVO> moves = new HashSet<>();
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity("https://pokeapi.co/api/v2/pokemon/{name}", Map.class, pokemonName);
            Map<String, Object> jsonResponse = response.getBody();
            if(jsonResponse == null) return new HashSet<>();
            if (response.getStatusCode().is2xxSuccessful()) {
                List<Map<String, Object>> allMoves = (List) jsonResponse.get("moves");
                for (int i = 0; i < Math.min(allMoves.size(), movesNumber); i++) {
                    Map<String, Object> moveObject = allMoves.get(i);
                    Map<String, String> moveMap = (Map) moveObject.get("move");
                    String moveName = moveMap.get("name");
                    MoveVO moveVO = getMoveByName(moveName);
                    moves.add(moveVO);
                }
            } else {
                log.error(ERROR_MESSAGE, response.getStatusCode());
            }

        } catch (Exception e) {
            log.error(ERROR_MESSAGE, e.getMessage());
        }
        return moves;
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
