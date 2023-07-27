package com.pokemon.ao.persistence.entity;

import com.pokemon.ao.domain.MoveSlot;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Entity
@SuperBuilder
@Getter
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "entity_type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "pokemon")
public class Pokemon implements EntityDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @Column(name = "name")
    protected String name;
    @ManyToOne
    @JoinColumn(name = "species_id")
    protected Species species;

    @Column ( name = "current_hp")
    protected int currentHp;

    @Column (name = "max_hp")
    protected int maxHp;

    @ManyToMany
    @JoinTable(name = "pokemon_moves",
            joinColumns = @JoinColumn(name = "pokemon_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "move_id", referencedColumnName = "id")
    )
    @MapKeyEnumerated(value = EnumType.STRING)
    @MapKeyColumn(name = "move_slot", table = "pokemon_moves")
    protected Map<MoveSlot, Move> moves;

    @Column(name = "original_trainer")
    protected String originalTrainer;
}
