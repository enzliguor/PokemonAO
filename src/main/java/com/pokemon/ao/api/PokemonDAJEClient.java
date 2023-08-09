package com.pokemon.ao.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokemon.ao.api.utility.ExchangeResponse;
import com.pokemon.ao.api.utility.ExchangeStatus;
import com.pokemon.ao.api.utility.NullExchangeResponse;
import com.pokemon.ao.config.CustomProperties;
import com.pokemon.ao.dto.PokemonDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.codec.DecodingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class PokemonDAJEClient {
    private final CustomProperties customProperties;
    private final WebClient webClient;
    @Autowired
    private PokemonDAJEClient(CustomProperties customProperties, ObjectMapper objectMapper) {
        this.customProperties = customProperties;
        String dajeBaseUrl = this.customProperties.getPokemonDajeBaseUrl();
        this.webClient = WebClient.builder()
                .baseUrl(dajeBaseUrl)
                .codecs(configurer -> configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper)))
                .build();
    }

    public ExchangeResponse exchange(PokemonDTO pokemonToExchange){
        String dajeExchangeUrl = this.customProperties.getPokemonDajeExchangeUrl();
        return this.webClient.post()
                    .uri(dajeExchangeUrl)
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue(pokemonToExchange)
                    .retrieve()
                    .bodyToMono(ExchangeResponse.class)
                    .onErrorResume(throwable -> {
                        if (throwable instanceof DecodingException e) {
                            log.error("Error parsing JSON response: unexpected data from PokemonDAJE ", e);
                        }else if (throwable instanceof WebClientRequestException e) {
                            log.error("Can't send a request to PokemonDAJE: ", e);
                        } else if (throwable instanceof WebClientResponseException e) {
                            log.error("Negative response from PokemonDAJE exchange: ", e);
                        } else {
                            log.error("Unexpected error: ", throwable);
                        }
                        return Mono.just(new NullExchangeResponse());
                    })
                    .block();
    }

    public HttpStatusCode communicateStatusExchange(String exchangeID, ExchangeStatus exchangeStatus){
        String pokemonDajeStatusExchangeUrl = this.customProperties.getPokemonDajeStatusExchangeUrl();
        return this.webClient.post()
                .uri(pokemonDajeStatusExchangeUrl, exchangeID)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(exchangeStatus.getStatusCode())
                .exchangeToMono(response -> Mono.just(response.statusCode()))
                .onErrorResume(throwable -> {
                    log.error("Error communicating exchange status to PokemonDAJE: ", throwable);
                    return Mono.just(HttpStatus.INTERNAL_SERVER_ERROR);
                })
                .block();
    }
}
