package com.pokemon.ao.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pokemon.ao.dto.utility.MoveDTODeserializer;
import lombok.*;

@Builder
@Getter
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonDeserialize(using = MoveDTODeserializer.class)
public class MoveDTO implements DTO {

    private String name;

    private int power;

    private String typeName;
}
