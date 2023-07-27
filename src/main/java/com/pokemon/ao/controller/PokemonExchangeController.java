package com.pokemon.ao.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pokemon.ao.config.CustomProperties;
import com.pokemon.ao.domain.PokemonVO;
import com.pokemon.ao.dto.PokemonDTO;
import com.pokemon.ao.dto.converter.PokemonConverterDTO;
import com.pokemon.ao.dto.utility.DTOValidator;
import com.pokemon.ao.persistence.service.PokemonService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@Transactional(rollbackFor = Exception.class)
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
        if (pokemonToExchange == null) {
            return ResponseEntity.badRequest().build();
        }

        PokemonDTO pokemonToExchangeDTO = this.pokemonConverterDTO.convertFromVOToDTO(pokemonToExchange);
        if (!this.dtoValidator.isValidPokemonDTO(pokemonToExchangeDTO)) {
            return ResponseEntity.badRequest().build();
        }

        ResponseEntity<ExchangeResponse> response = callRemoteExchange(pokemonToExchangeDTO);
        ExchangeResponse exchangeResponse = response.getBody();
        if (exchangeResponse != null) {
            PokemonDTO receivedPokemonDTO = exchangeResponse.getPokemon();

            if (!this.dtoValidator.isValidPokemonDTO(receivedPokemonDTO)) {
                communicateStatusExchange(exchangeResponse.getExchangeId(), new StatusExchange(400));
                return ResponseEntity.internalServerError().body(pokemonToExchange);
            }
            try {
                PokemonVO exchangedPokemon = performExchange(pokemonToExchange, receivedPokemonDTO, exchangeResponse.getExchangeId());
                return ResponseEntity.ok().body(exchangedPokemon);
            } catch (Exception exception) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                try{
                    communicateStatusExchange(exchangeResponse.getExchangeId(), new StatusExchange(500));
                }catch (RestClientException e){
                    return ResponseEntity.internalServerError().body(null);
                }
                return ResponseEntity.internalServerError().body(null);
            }
        }
        return ResponseEntity.internalServerError().body(null);
    }
    private ResponseEntity<ExchangeResponse> callRemoteExchange(PokemonDTO pokemonToExchangeDTO) {
        String pokemonDajeExchangeUrl = this.customProperties.getPokemonDajeExchangeUrl();
        ResponseEntity<ExchangeResponse> response;
        try {
            response = restTemplate.postForEntity(pokemonDajeExchangeUrl, pokemonToExchangeDTO, ExchangeResponse.class);
        } catch (RestClientException e) {
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

    private ResponseEntity<Void> communicateStatusExchange(String exchangeID, StatusExchange statusExchange) {
        String pokemonDajeStatusExchangeUrl = this.customProperties.getPokemonDajeStatusExchangeUrl();
        String constructedUrl = UriComponentsBuilder.fromUriString(pokemonDajeStatusExchangeUrl).buildAndExpand(exchangeID).toUriString();
        HttpEntity<StatusExchange> requestEntity = new HttpEntity<>(statusExchange);
        return restTemplate.exchange(constructedUrl, HttpMethod.POST, requestEntity, Void.class);
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
