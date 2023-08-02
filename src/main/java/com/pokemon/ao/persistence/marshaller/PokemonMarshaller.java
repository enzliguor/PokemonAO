package com.pokemon.ao.persistence.marshaller;

import com.pokemon.ao.domain.PokemonVO;
import com.pokemon.ao.domain.UnknownPokemonVO;
import com.pokemon.ao.persistence.entity.Pokemon;
import com.pokemon.ao.persistence.entity.UnknownPokemon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PokemonMarshaller implements Marshaller<PokemonVO, Pokemon> {

    private final MoveMarshaller moveMarshaller;
    private final SpeciesMarshaller speciesMarshaller;
    private final UnknownPokemonMarshaller unknownPokemonMarshaller;
    @Autowired
    private PokemonMarshaller(MoveMarshaller moveMarshaller, SpeciesMarshaller speciesMarshaller, UnknownPokemonMarshaller unknownPokemonMarshaller) {
        this.moveMarshaller = moveMarshaller;
        this.speciesMarshaller = speciesMarshaller;
        this.unknownPokemonMarshaller = unknownPokemonMarshaller;
    }


    @Override
    public Pokemon marshall(PokemonVO pokemonVO) {
        if (pokemonVO instanceof UnknownPokemonVO unknownPokemonVO) {
            return this.unknownPokemonMarshaller.marshall(unknownPokemonVO);
        }
        return Pokemon.builder()
                .id(pokemonVO.getId())
                .name(pokemonVO.getName())
                .species(speciesMarshaller.marshall(pokemonVO.getSpecies()))
                .currentHp(pokemonVO.getCurrentHp())
                .maxHp(pokemonVO.getMaxHp())
                .moves(pokemonVO.getMoves().entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> this.moveMarshaller.marshall(entry.getValue())
                        )))
                .originalTrainer(pokemonVO.getOriginalTrainer())
                .build();
    }

    @Override
    public PokemonVO unmarshall(Pokemon pokemon) {
        if (pokemon instanceof UnknownPokemon unknownPokemon) {
            return this.unknownPokemonMarshaller.unmarshall(unknownPokemon);
        }
        return PokemonVO.builder()
                .id(pokemon.getId())
                .name(pokemon.getName())
                .species(speciesMarshaller.unmarshall(pokemon.getSpecies()))
                .currentHp(pokemon.getCurrentHp())
                .maxHp(pokemon.getMaxHp())
                .moves(pokemon.getMoves().entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> this.moveMarshaller.unmarshall(entry.getValue())
                        ))).originalTrainer(pokemon.getOriginalTrainer())
                .build();
    }
}