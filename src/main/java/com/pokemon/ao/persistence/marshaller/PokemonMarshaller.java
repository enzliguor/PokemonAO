package com.pokemon.ao.persistence.marshaller;

import com.pokemon.ao.domain.PokemonVO;
import com.pokemon.ao.persistence.entity.Pokemon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class PokemonMarshaller implements Marshaller<PokemonVO, Pokemon> {

    private final MoveMarshaller moveMarshaller;
    private final SpeciesMarshaller speciesMarshaller;

    @Autowired
    private PokemonMarshaller(MoveMarshaller moveMarshaller, SpeciesMarshaller speciesMarshaller) {
        this.moveMarshaller = moveMarshaller;
        this.speciesMarshaller = speciesMarshaller;
    }

    @Override
    public Pokemon marshall(PokemonVO pokemonVO) {
        return Pokemon.builder()
                .id(pokemonVO.getId())
                .name(pokemonVO.getName())
                .species(speciesMarshaller.marshall(pokemonVO.getSpecies()))
                .currentHp(pokemonVO.getCurrentHp())
                .maxHp(pokemonVO.getMaxHp())
                .moves(pokemonVO.getMoves().stream().map(moveMarshaller::marshall).collect(Collectors.toSet()))
                .originalTrainer(pokemonVO.getOriginalTrainer())
                .build();
    }

    @Override
    public PokemonVO unmarshall(Pokemon pokemon) {
       return PokemonVO.builder()
               .id(pokemon.getId())
               .name(pokemon.getName())
               .species(speciesMarshaller.unmarshall(pokemon.getSpecies()))
               .currentHp(pokemon.getCurrentHp())
               .maxHp(pokemon.getMaxHp())
               .moves(pokemon.getMoves().stream().map(moveMarshaller::unmarshall).collect(Collectors.toSet()))
               .originalTrainer(pokemon.getOriginalTrainer())
               .build();
    }
}