package com.pokemon.ao.domain;

import lombok.*;

@Getter
@Builder
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SpeciesVO implements ValueObject {
    private Integer id;
    private String spriteUrl;
    private String name;
    private TypeVO type;
}
