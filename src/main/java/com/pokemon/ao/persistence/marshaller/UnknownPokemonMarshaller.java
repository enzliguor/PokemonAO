package com.pokemon.ao.persistence.marshaller;

import com.pokemon.ao.domain.UnknownPokemonVO;
import com.pokemon.ao.persistence.entity.UnknownPokemon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UnknownPokemonMarshaller implements Marshaller<UnknownPokemonVO, UnknownPokemon>{
    private final SpeciesMarshaller speciesMarshaller;
    private final MoveMarshaller moveMarshaller;
    @Autowired
    private UnknownPokemonMarshaller(SpeciesMarshaller speciesMarshaller, MoveMarshaller moveMarshaller) {
        this.speciesMarshaller = speciesMarshaller;
        this.moveMarshaller = moveMarshaller;
    }


    @Override
    public UnknownPokemon marshall(UnknownPokemonVO unknownPokemonVO) {
        return UnknownPokemon.builder()
                .id(unknownPokemonVO.getId())
                .name(unknownPokemonVO.getName())
                .species(speciesMarshaller.marshall(unknownPokemonVO.getSpecies()))
                .currentHp(unknownPokemonVO.getCurrentHp())
                .maxHp(unknownPokemonVO.getMaxHp())
                .moves(unknownPokemonVO.getMoves().entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> this.moveMarshaller.marshall(entry.getValue())
                        )))
                .originalTrainer(unknownPokemonVO.getOriginalTrainer())
                .originalSpeciesID(unknownPokemonVO.getOriginalSpeciesID())
                .build();
    }

    @Override
    public UnknownPokemonVO unmarshall(UnknownPokemon unknownPokemon) {
        return UnknownPokemonVO.builder()
                .id(unknownPokemon.getId())
                .name(unknownPokemon.getName())
                .species(speciesMarshaller.unmarshall(unknownPokemon.getSpecies()))
                .currentHp(unknownPokemon.getCurrentHp())
                .maxHp(unknownPokemon.getMaxHp())
                .moves(unknownPokemon.getMoves().entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> this.moveMarshaller.unmarshall(entry.getValue())
                        )))
                .originalTrainer(unknownPokemon.getOriginalTrainer())
                .originalSpeciesID(unknownPokemon.getOriginalSpeciesID())
                .build();
    }
}
