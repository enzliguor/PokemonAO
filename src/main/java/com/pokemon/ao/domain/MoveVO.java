package com.pokemon.ao.domain;

import lombok.*;

@Getter
@Builder
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MoveVO implements ValueObject {
    private Integer id;
    private String name;
    private TypeVO type;
    private int power;
}
