package com.pokemon.ao.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pokemon.ao.domain.PokemonVO;
import com.pokemon.ao.dto.PokemonDTO;
import com.pokemon.ao.dto.converter.PokemonConverterDTO;
import com.pokemon.ao.dto.utility.DTOValidator;
import com.pokemon.ao.persistence.service.PokemonService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/pokemon")
public class PokemonExchangeController {

    private final RestTemplate restTemplate;
    private final PokemonService pokemonService;
    private final PokemonConverterDTO pokemonConverterDTO;
    private final DTOValidator dtoValidator;

    @Autowired
    public PokemonExchangeController(RestTemplate restTemplate, PokemonService pokemonService, PokemonConverterDTO pokemonConverterDTO, DTOValidator dtoValidator) {
        this.restTemplate = restTemplate;
        this.pokemonService = pokemonService;
        this.pokemonConverterDTO = pokemonConverterDTO;
        this.dtoValidator = dtoValidator;
    }

    @PostMapping("/exchange/{id}")
    public ResponseEntity<PokemonVO> exchangePokemonRandom(@PathVariable Integer id){
        PokemonVO pokemonToExchange = this.pokemonService.findById(id);
        PokemonDTO pokemonDTO = this.pokemonConverterDTO.convertFromVOToDTO(pokemonToExchange);
        PokemonVO newPokemon = null;
        if(!this.dtoValidator.isValidPokemonDTO(pokemonDTO)){
            return ResponseEntity.badRequest().body(pokemonToExchange);
        }
        String url = "http://localhost:8080/api/pokemon/exchange";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PokemonDTO> httpEntity = new HttpEntity<>(pokemonDTO, headers);
        ResponseEntity<ExchangeResponse> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ExchangeResponse.class);
        if(response.getStatusCode() == HttpStatusCode.valueOf(400)){
            return ResponseEntity.internalServerError().body(pokemonToExchange);
        } else if (response.getStatusCode()==HttpStatusCode.valueOf(500) || response.getBody() == null) {
            return ResponseEntity.internalServerError().body(pokemonToExchange);
        }else if (response.getStatusCode() == HttpStatusCode.valueOf(200)){
            ExchangeResponse exchangeResponse = response.getBody();
            PokemonDTO receivedPokemonDTO = exchangeResponse.pokemon;
            if(!this.dtoValidator.isValidPokemonDTO(receivedPokemonDTO)){
                this.communicateStatusExchange(exchangeResponse.exchangeId, new StatusExchange(400));
                return ResponseEntity.internalServerError().body(pokemonToExchange);
            }
            PokemonVO receivedPokemonVO = this.pokemonConverterDTO.convertFromDTOToVO(receivedPokemonDTO);
            try{
                newPokemon = this.pokemonService.save(receivedPokemonVO);
                this.pokemonService.delete(pokemonToExchange.getId());
                this.communicateStatusExchange(exchangeResponse.exchangeId, new StatusExchange(200));
            }catch (Exception exception){
                this.communicateStatusExchange(exchangeResponse.exchangeId, new StatusExchange(500));
                if(newPokemon != null) this.pokemonService.delete(newPokemon.getId());
                if(this.pokemonService.findById(pokemonToExchange.getId()) == null) pokemonToExchange = this.pokemonService.save(pokemonToExchange);
                return ResponseEntity.internalServerError().body(pokemonToExchange);
            }

        }
        return ResponseEntity.ok().body(newPokemon);
    }

    private ResponseEntity<HttpStatusCode> communicateStatusExchange(String exchangeID, StatusExchange statusExchange){
        String url = "http://localhost:8080/api/pokemon/{exchangeID}/status";
        String constructedUrl = UriComponentsBuilder.fromUriString(url).buildAndExpand(exchangeID).toUriString();
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
