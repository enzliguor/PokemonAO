package com.pokemon.ao.api;

import com.pokemon.ao.dto.SpeciesDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@Slf4j
public class PokemonApiClient {
    private final RestTemplate restTemplate;

    @Autowired
    private PokemonApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Set<SpeciesDTO> getSpecies(int numberOfSpecies) {
        return IntStream.rangeClosed(1, numberOfSpecies)
                .mapToObj(speciesID -> invokeApi("https://pokeapi.co/api/v2/pokemon/{id}", Map.class, speciesID))
                .filter(Objects::nonNull)
                .map(jsonResponse -> SpeciesDTO.builder()
                        .name((String) jsonResponse.get("name"))
                        .spriteUrl(this.getFrontDefaultSprite((Map) jsonResponse.get("sprites")))
                        .type(this.getTypeName((List) jsonResponse.get("types")))
                        .build())
                .collect(Collectors.toSet());
    }

    private String getFrontDefaultSprite(Map<String, Map<String, Map<String, String>>> sprites) {
        if (sprites == null) return null;
        return sprites.get("other")
                .get("home")
                .get("front_default");
    }


    private String getTypeName(List<Map<String, Object>> types) {
        if (types == null || types.isEmpty()) {
            return null;
        }
        Map<String, Object> typeObj = types.get(0);
        Map<String, Object> typeMap = (Map<String, Object>) typeObj.get("type");
        return (String) typeMap.get("name");
    }

    private <T> T invokeApi(String url, Class<T> responseType, Object... urlVariables) {
        try {
            String constructedUrl = UriComponentsBuilder.fromUriString(url).buildAndExpand(urlVariables).toUriString();
            ResponseEntity<T> response = restTemplate.getForEntity(constructedUrl, responseType);
            log.info("Invoking {}", url);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                String errorMessage = "Error calling this URL: " + url + " Status Code: " + response.getStatusCode();
                log.error(errorMessage);
            }
        } catch (RestClientException e) {
            String errorMessage = "Error calling this URL: " + url;
            log.error(errorMessage, e);
        }
        return null;
    }
}
