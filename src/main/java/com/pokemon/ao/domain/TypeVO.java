package com.pokemon.ao.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TypeVO implements ValueObject {
    private Long id;
    private String name;
    private String icon;
}
