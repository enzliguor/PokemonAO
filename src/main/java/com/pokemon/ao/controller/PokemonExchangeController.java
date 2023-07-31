package com.pokemon.ao.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokemon.ao.config.CustomProperties;
import com.pokemon.ao.domain.PokemonVO;
import com.pokemon.ao.dto.PokemonDTO;
import com.pokemon.ao.dto.converter.PokemonConverterDTO;
import com.pokemon.ao.dto.utility.DTOValidator;
import com.pokemon.ao.persistence.service.PokemonService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@Transactional(rollbackFor = Exception.class)
@RequestMapping("/api/pokemon")
@CrossOrigin("*")
@Slf4j
public class PokemonExchangeController {

    private final RestTemplate restTemplate;
    private final PokemonService pokemonService;
    private final PokemonConverterDTO pokemonConverterDTO;
    private final DTOValidator dtoValidator;
    private final CustomProperties customProperties;
    private final ObjectMapper objectMapper;

    @Autowired
    public PokemonExchangeController(RestTemplate restTemplate, PokemonService pokemonService, PokemonConverterDTO pokemonConverterDTO, DTOValidator dtoValidator, CustomProperties customProperties, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.pokemonService = pokemonService;
        this.pokemonConverterDTO = pokemonConverterDTO;
        this.dtoValidator = dtoValidator;
        this.customProperties = customProperties;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/exchange/{id}")
    public ResponseEntity<PokemonVO> exchangePokemon(@PathVariable Integer id) {
        PokemonVO pokemonToExchange = this.pokemonService.findById(id);
        if (pokemonToExchange == null) {
            log.error("Pokemon NOT FOUND in DB with id: {}", id);
            return ResponseEntity.badRequest().build();
        }

        PokemonDTO pokemonToExchangeDTO = this.pokemonConverterDTO.convertFromVOToDTO(pokemonToExchange);
        if (!this.dtoValidator.isValidPokemonDTO(pokemonToExchangeDTO)) {
            log.error("Trying to exchange INVALID POKEMON with id: {}", id);
            return ResponseEntity.badRequest().build();
        }

        ResponseEntity<String> response = callRemoteExchange(pokemonToExchangeDTO);
        ExchangeResponse exchangeResponse;
        try {
            exchangeResponse = this.objectMapper.readValue(response.getBody(), ExchangeResponse.class);
        } catch (JsonProcessingException e) {
            log.error("UNEXPECTED DATA FROM PokemonDAJE");
            return ResponseEntity.internalServerError().body(pokemonToExchange);
        }
        if (exchangeResponse != null) {
            PokemonDTO receivedPokemonDTO = exchangeResponse.getPokemon();

            if (!this.dtoValidator.isValidPokemonDTO(receivedPokemonDTO)) {
                log.error("RECEIVED INVALID POKEMON from remote exchange with id: {}", exchangeResponse.getExchangeId());
                log.error("Sending STATUS CODE 400 to PokemonDAJE");
                communicateStatusExchange(exchangeResponse.getExchangeId(), new StatusExchange(400));
                return ResponseEntity.internalServerError().body(pokemonToExchange);
            }
            try {
                PokemonVO exchangedPokemon = performExchange(pokemonToExchange, receivedPokemonDTO, exchangeResponse.getExchangeId());
                log.info("EXCHANGE SUCCESS: {}", exchangedPokemon);
                return ResponseEntity.ok().body(exchangedPokemon);
            } catch (Exception exception) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                try {
                    communicateStatusExchange(exchangeResponse.getExchangeId(), new StatusExchange(500));
                    log.error("COMMUNICATING STATUS CODE 500 TO PokemonDAJE");
                    return ResponseEntity.internalServerError().body(null);
                } catch (RestClientException e) {
                    log.error("ERROR TRYING TO COMMUNICATE STATUS EXCHANGE TO PokemonDAJE {}", e.getMessage());
                    return ResponseEntity.internalServerError().body(null);
                }
            }
        }
        log.error("NEGATIVE RESPONSE RECEIVED from remote exchange");
        return ResponseEntity.internalServerError().body(null);
    }

    private ResponseEntity<String> callRemoteExchange(PokemonDTO pokemonToExchangeDTO) {
        String pokemonDajeExchangeUrl = this.customProperties.getPokemonDajeExchangeUrl();
        ResponseEntity<String> response;
        try {
            response = restTemplate.postForEntity(pokemonDajeExchangeUrl, pokemonToExchangeDTO, String.class);
        } catch (RestClientException e) {
            log.error("ERROR CALLING {}", pokemonDajeExchangeUrl);
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
        return response;
    }

    public PokemonVO performExchange(PokemonVO pokemonToExchange, PokemonDTO receivedPokemonDTO, String exchangeID) {
        PokemonVO receivedPokemonVO = this.pokemonConverterDTO.convertFromDTOToVO(receivedPokemonDTO);
        PokemonVO newPokemon = this.pokemonService.save(receivedPokemonVO);
        this.pokemonService.delete(pokemonToExchange.getId());

        communicateStatusExchange(exchangeID, new StatusExchange(200));
        return newPokemon;
    }

    private void communicateStatusExchange(String exchangeID, StatusExchange statusExchange) {
        String pokemonDajeStatusExchangeUrl = this.customProperties.getPokemonDajeStatusExchangeUrl();
        String constructedUrl = UriComponentsBuilder.fromUriString(pokemonDajeStatusExchangeUrl).buildAndExpand(exchangeID).toUriString();
        HttpEntity<StatusExchange> requestEntity = new HttpEntity<>(statusExchange);
        restTemplate.exchange(constructedUrl, HttpMethod.POST, requestEntity, Void.class);
    }

    @Getter
    private static class ExchangeResponse {
        private PokemonDTO pokemon;
        @JsonProperty("exchange_id")
        private String exchangeId;

    }

    @Getter
    @AllArgsConstructor
    private static class StatusExchange {
        @JsonProperty("status_code")
        private int statusCode;

    }
}
