package com.pokemon.ao.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "type")
public class Type  implements EntityDB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column (name = "name")
    private String name;

    @Column(name = "icon")
    private String icon;
}
