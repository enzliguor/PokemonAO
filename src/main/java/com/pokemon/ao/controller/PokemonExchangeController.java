package com.pokemon.ao.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pokemon.ao.config.CustomProperties;
import com.pokemon.ao.domain.PokemonVO;
import com.pokemon.ao.dto.PokemonDTO;
import com.pokemon.ao.dto.converter.PokemonConverterDTO;
import com.pokemon.ao.dto.utility.DTOValidator;
import com.pokemon.ao.exception.ExchangeStatusException;
import com.pokemon.ao.persistence.service.PokemonService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@Transactional(rollbackFor = {RuntimeException.class, Exception.class})
@RequestMapping("/api/pokemon")
public class PokemonExchangeController {

    private final RestTemplate restTemplate;
    private final PokemonService pokemonService;
    private final PokemonConverterDTO pokemonConverterDTO;
    private final DTOValidator dtoValidator;
    private final CustomProperties customProperties;

    @Autowired
    public PokemonExchangeController(RestTemplate restTemplate, PokemonService pokemonService, PokemonConverterDTO pokemonConverterDTO, DTOValidator dtoValidator, CustomProperties customProperties) {
        this.restTemplate = restTemplate;
        this.pokemonService = pokemonService;
        this.pokemonConverterDTO = pokemonConverterDTO;
        this.dtoValidator = dtoValidator;
        this.customProperties = customProperties;
    }
    @PostMapping("/exchange/{id}")
    public ResponseEntity<PokemonVO> exchangePokemon(@PathVariable Integer id) {
        PokemonVO pokemonToExchange = this.pokemonService.findById(id);
        PokemonDTO pokemonToExchangeDTO = this.pokemonConverterDTO.convertFromVOToDTO(pokemonToExchange);

        if (!this.dtoValidator.isValidPokemonDTO(pokemonToExchangeDTO)) {
            return ResponseEntity.badRequest().body(pokemonToExchange);
        }
        String pokemonDajeExchangeUrl = this.customProperties.getPokemonDajeExchangeUrl();

        ResponseEntity<ExchangeResponse> response = restTemplate.postForEntity(pokemonDajeExchangeUrl, pokemonToExchangeDTO, ExchangeResponse.class);
        HttpStatusCode statusCode = response.getStatusCode();
        ExchangeResponse exchangeResponse = response.getBody();

        if (statusCode == HttpStatus.OK && exchangeResponse!=null){
            PokemonDTO receivedPokemonDTO = exchangeResponse.getPokemon();

            if (!this.dtoValidator.isValidPokemonDTO(receivedPokemonDTO)) {
                communicateStatusExchange(exchangeResponse.getExchangeId(), new StatusExchange(400));
                return ResponseEntity.internalServerError().body(pokemonToExchange);
            }

            try {
                PokemonVO exchangedPokemon = performExchange(pokemonToExchange, receivedPokemonDTO, exchangeResponse.getExchangeId());
                return ResponseEntity.ok().body(exchangedPokemon);
            } catch (Exception exception) {
                communicateStatusExchange(exchangeResponse.getExchangeId(), new StatusExchange(500));
                return ResponseEntity.internalServerError().body(pokemonToExchange);
            }
        }
        return ResponseEntity.internalServerError().body(pokemonToExchange);

    }

    public PokemonVO performExchange(PokemonVO pokemonToExchange, PokemonDTO receivedPokemonDTO, String exchangeID) throws ExchangeStatusException {
        PokemonVO receivedPokemonVO = this.pokemonConverterDTO.convertFromDTOToVO(receivedPokemonDTO);
        PokemonVO newPokemon = this.pokemonService.save(receivedPokemonVO);
        this.pokemonService.delete(pokemonToExchange.getId());
        ResponseEntity<HttpStatusCode> response = communicateStatusExchange(exchangeID, new StatusExchange(200));
        HttpStatusCode statusCode = response.getStatusCode();
        if (statusCode != HttpStatus.OK) {
            throw new ExchangeStatusException("Errore durante la comunicazione dello stato dell'exchange");
        }
        return newPokemon;
    }

    private ResponseEntity<HttpStatusCode> communicateStatusExchange(String exchangeID, StatusExchange statusExchange) {
        String pokemonDajeStatusExchangeUrl = this.customProperties.getPokemonDajeStatusExchangeUrl();
        String constructedUrl = UriComponentsBuilder.fromUriString(pokemonDajeStatusExchangeUrl).buildAndExpand(exchangeID).toUriString();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<StatusExchange> httpEntity = new HttpEntity<>(statusExchange, headers);
        return restTemplate.exchange(constructedUrl, HttpMethod.POST, httpEntity, HttpStatusCode.class);
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
