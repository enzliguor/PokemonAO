package com.pokemon.ao.config;

import com.pokemon.ao.domain.PokemonVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomProperties {
    private PokemonVO pokemon;
}
