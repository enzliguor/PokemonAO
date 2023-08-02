package com.pokemon.ao.dto;

import lombok.*;

@Builder
@Getter
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SpeciesDTO implements DTO {

    private String name;

    private String spriteUrl;

    private String type;
}
