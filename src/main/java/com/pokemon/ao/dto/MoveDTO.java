package com.pokemon.ao.dto;

import lombok.*;

@Builder
@Getter
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MoveDTO implements DTO {

    private String name;

    private int power;

    private String typeName;
}
