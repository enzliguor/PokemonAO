package com.pokemon.ao.api.utility;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pokemon.ao.dto.PokemonDTO;
import lombok.Getter;
@Getter
public class ExchangeResponse {
        private PokemonDTO pokemon;
        @JsonProperty("exchange_id")
        private String exchangeId;

        public boolean isNull(){
                return false;
        }
}
