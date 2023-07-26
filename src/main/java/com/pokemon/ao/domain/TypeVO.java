package com.pokemon.ao.domain;

import lombok.*;


@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TypeVO implements ValueObject {
    private Integer id;
    private String name;
    private String icon;
}
