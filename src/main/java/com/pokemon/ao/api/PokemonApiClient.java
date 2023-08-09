package com.pokemon.ao.api;

import com.pokemon.ao.config.CustomProperties;
import com.pokemon.ao.domain.TypeVO;
import com.pokemon.ao.dto.MoveDTO;
import com.pokemon.ao.dto.SpeciesDTO;
import com.pokemon.ao.dto.TypeDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@Slf4j
public class PokemonApiClient {
    private final WebClient webClient;

    private final String pokemonApiBaseUrl;

    private final CustomProperties customProperties;

    @Autowired
    private PokemonApiClient(CustomProperties customProperties) {
        this.customProperties = customProperties;
        this.pokemonApiBaseUrl = this.customProperties.getPokemonApiBaseUrl();
        this.webClient = WebClient.builder()
                .baseUrl(pokemonApiBaseUrl)
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(2 * 1024 * 1024))
                .build();
    }

    public Set<SpeciesDTO> getSpecies(int numberOfSpecies) {
        String pokemonApiSpeciesUrl = this.customProperties.getPokemonApiSpeciesUrl();
        return IntStream.rangeClosed(1, numberOfSpecies)
                .parallel()
                .mapToObj(speciesID -> invokeApi(pokemonApiSpeciesUrl, SpeciesDTO.class, speciesID))
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<MoveDTO> getTypesMoves(List<TypeVO> typeVO, int maxMoves) {
        String pokemonApiTypeUrl = this.customProperties.getPokemonApiTypesUrl();
        int maxMovesPerType = (maxMoves + typeVO.size() - 1) / typeVO.size();
        return typeVO.stream()
                .parallel()
                .map(type -> invokeApi(pokemonApiTypeUrl, TypeDTO.class, type.getName()))
                .filter(Objects::nonNull)
                .flatMap(typeDTO -> this.getMoves(typeDTO.getMovesName(), maxMovesPerType).stream())
                .limit(maxMoves)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private Set<MoveDTO> getMoves(Set<String> movesName, int maxMovesPerType) {
        String pokemonApiMoveUrl = this.customProperties.getPokemonApiMovesUrl();
        return movesName.stream()
                .limit(maxMovesPerType)
                .map(name -> this.invokeApi(pokemonApiMoveUrl, MoveDTO.class, name))
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));

    }

    private <T> T invokeApi(String url, Class<T> responseType, Object... urlVariables) {
        String constructedUrl = UriComponentsBuilder.fromUriString(url).buildAndExpand(urlVariables).toUriString();
        log.info("Calling {}", this.pokemonApiBaseUrl + constructedUrl);
        Mono<T> responseMono = webClient.get()
                .uri(constructedUrl)
                .retrieve()
                .bodyToMono(responseType)
                .onErrorResume(throwable -> {
                    log.error("Error calling this URL: {}", this.pokemonApiBaseUrl + constructedUrl, throwable);
                    return Mono.empty();
                });
        return responseMono.block();
    }
}
