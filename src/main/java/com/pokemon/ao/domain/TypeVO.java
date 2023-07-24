package com.pokemon.ao.domain;

import lombok.*;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TypeVO implements ValueObject {
    private Integer id;
    private String name;
    private String icon;
}
