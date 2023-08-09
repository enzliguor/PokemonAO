package com.pokemon.ao.utility;

import com.pokemon.ao.api.PokemonDAJEClient;
import com.pokemon.ao.api.utility.ExchangeResponse;
import com.pokemon.ao.api.utility.ExchangeStatus;
import com.pokemon.ao.domain.PokemonVO;
import com.pokemon.ao.dto.PokemonDTO;
import com.pokemon.ao.dto.converter.PokemonConverterDTO;
import com.pokemon.ao.dto.utility.DTOValidator;
import com.pokemon.ao.persistence.service.PokemonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Component
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ExchangePokemonHandler {
    private final PokemonDAJEClient pokemonDAJEClient;
    private final PokemonConverterDTO pokemonConverterDTO;
    private final DTOValidator dtoValidator;
    private final PokemonService pokemonService;

    protected ExchangePokemonHandler(PokemonDAJEClient pokemonDAJEClient, PokemonConverterDTO pokemonConverterDTO, DTOValidator dtoValidator, PokemonService pokemonService) {
        this.pokemonDAJEClient = pokemonDAJEClient;
        this.pokemonConverterDTO = pokemonConverterDTO;
        this.dtoValidator = dtoValidator;
        this.pokemonService = pokemonService;
    }

    public PokemonVO exchange(Integer pokemonID) {
        PokemonVO pokemonToExchange = this.pokemonService.findById(pokemonID);
        if (pokemonToExchange == null) {
            log.error("Pokemon not found in db with id: {}", pokemonID);
            return null;
        }
        PokemonDTO pokemonToExchangeDTO = this.pokemonConverterDTO.convertFromVOToDTO(pokemonToExchange);
        if (!this.dtoValidator.isValidPokemonDTO(pokemonToExchangeDTO)) {
            log.error("Trying to exchange invalid Pokemon with id: {}", pokemonID);
            return null;
        }

        ExchangeResponse exchangeResponse = this.pokemonDAJEClient.exchange(pokemonToExchangeDTO);
        if (exchangeResponse.isNull()) return null;

        PokemonDTO receivedPokemonDTO = exchangeResponse.getPokemon();

        if (!this.dtoValidator.isValidPokemonDTO(receivedPokemonDTO)) {
            log.error("Received invalid Pokemon from PokemonDAJE exchange with id: {}. Sending STATUS CODE {} to PokemonDAJE", exchangeResponse.getExchangeId(), ExchangeStatus.INVALID);
            this.pokemonDAJEClient.communicateStatusExchange(exchangeResponse.getExchangeId(), ExchangeStatus.INVALID);
            return null;
        }
        PokemonVO receivedPokemonVO = this.pokemonConverterDTO.convertFromDTOToVO(receivedPokemonDTO);
        receivedPokemonVO = this.pokemonService.save(receivedPokemonVO);
        this.pokemonService.deleteByID(pokemonID);

        HttpStatusCode statusCode = this.pokemonDAJEClient.communicateStatusExchange(exchangeResponse.getExchangeId(), ExchangeStatus.SUCCESS);
        if(!statusCode.equals(HttpStatus.OK)){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return null;
        }
        log.info("Pokemon with id {} successfully exchanged with pokemon: {}", pokemonID, receivedPokemonVO);

        return receivedPokemonVO;
    }
}
