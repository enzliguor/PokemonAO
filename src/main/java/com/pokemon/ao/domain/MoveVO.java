package com.pokemon.ao.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MoveVO implements ValueObject{
    private Long id;
    private String name;
    private TypeVO type;
    private int power;
}
