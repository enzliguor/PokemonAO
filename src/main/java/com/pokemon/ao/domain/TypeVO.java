package com.pokemon.ao.domain;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypeVO implements ValueObject {
    private Long id;
    private String name;
    private String icon;
}
