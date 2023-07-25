package com.pokemon.ao.config;

import com.pokemon.ao.domain.PokemonVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomProperties {
    private PokemonVO pokemon;

    private String urlDaje;

    private Map<String, String> icons;

    private Integer speciesCount;

    private Integer movesCount;

    private String pokeApiUrl;

    private int teamSize;
}
