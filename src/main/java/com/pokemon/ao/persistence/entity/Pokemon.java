package com.pokemon.ao.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "pokemon")
public class Pokemon implements EntityDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;
    @ManyToOne
    @JoinColumn(name = "species_id")
    private Species species;

    @Column ( name = "current_hp")
    private int currentHp;

    @Column (name = "max_hp")
    private int maxHp;

    @ManyToMany
    @JoinTable(
            name = "pokemon_moves",
            joinColumns = @JoinColumn(name = "pokemon_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "move_id", referencedColumnName = "id")
    )
    private Set<Move> moves;

    @Column(name = "original_trainer")
    private String originalTrainer;
}
