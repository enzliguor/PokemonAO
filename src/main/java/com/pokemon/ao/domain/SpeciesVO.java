package com.pokemon.ao.domain;

import lombok.*;


@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SpeciesVO implements ValueObject {
    private Integer id;
    private String spriteUrl;
    private String name;
    private TypeVO type;
}
