package com.pokemon.ao.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpeciesVO implements ValueObject{

    private Integer id;
    private String spriteUrl;
    private String name;
    private TypeVO type;
}
