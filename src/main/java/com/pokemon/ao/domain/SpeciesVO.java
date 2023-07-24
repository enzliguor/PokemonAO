package com.pokemon.ao.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SpeciesVO implements ValueObject{

    private Integer id;
    private String spriteUrl;
    private String name;
    private TypeVO type;
}