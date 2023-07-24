package com.pokemon.ao.domain;

import lombok.*;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MoveVO implements ValueObject {
    private Integer id;
    private String name;
    private TypeVO type;
    private int power;
}
