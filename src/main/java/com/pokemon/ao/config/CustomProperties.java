package com.pokemon.ao.config;

import com.pokemon.ao.domain.PokemonVO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomProperties {
    private PokemonVO defaultPokemon;
    private Map<String, String> typeIcons;
    private Integer speciesCount;
    private Integer movesCount;
    private int teamSize;
    private String pokemonDajeExchangeUrl;
    private String pokemonDajeStatusExchangeUrl;
    private int maxMovesPerPokemon;
    private int minMovesPerPokemon;
    private String pokemonApiSpeciesUrl;
    private String pokemonApiMovesUrl;
    private String pokemonApiTypesUrl;

}
