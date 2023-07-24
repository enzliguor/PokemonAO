package com.pokemon.ao.domain;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoveVO implements ValueObject{
    private Integer id;
    private String name;
    private TypeVO type;
    private int power;
}
