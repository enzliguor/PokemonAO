package com.pokemon.ao.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "move")
public class Move implements EntityDB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private Type type;

    @Column(name = "power")
    private int power;


}
