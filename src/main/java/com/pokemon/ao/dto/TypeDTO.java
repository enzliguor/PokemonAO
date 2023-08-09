package com.pokemon.ao.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pokemon.ao.dto.utility.TypeDTODeserializer;
import lombok.*;

import java.util.Set;
@Builder
@Getter
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonDeserialize(using = TypeDTODeserializer.class)
public class TypeDTO {
    private String name;
    private Set<String> movesName;
}
