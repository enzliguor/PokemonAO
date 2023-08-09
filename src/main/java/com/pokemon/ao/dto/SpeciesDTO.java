package com.pokemon.ao.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pokemon.ao.dto.utility.SpeciesDTODeserializer;
import lombok.*;

@Builder
@Getter
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonDeserialize(using = SpeciesDTODeserializer.class)
public class SpeciesDTO implements DTO {
    private String name;
    private String spriteUrl;
    private String type;
}
