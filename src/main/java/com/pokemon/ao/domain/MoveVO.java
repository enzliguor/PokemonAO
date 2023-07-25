package com.pokemon.ao.domain;

import lombok.*;


@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MoveVO implements ValueObject {
    private Integer id;
    private String name;
    private TypeVO type;
    private int power;
}
