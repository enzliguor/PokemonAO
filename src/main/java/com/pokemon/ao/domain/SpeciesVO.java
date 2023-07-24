package com.pokemon.ao.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SpeciesVO implements ValueObject{

    private Long id;
    private String sprite;
    private String name;
    private TypeVO type;
}
