package com.pokemon.ao.persistence.marshaller;

import com.pokemon.ao.domain.MoveVO;
import com.pokemon.ao.domain.PokemonVO;
import com.pokemon.ao.domain.SpeciesVO;
import com.pokemon.ao.persistence.entity.Pokemon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
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
        Pokemon pokemon = new Pokemon();
        pokemon.setId(pokemonVO.getId());
        pokemon.setName(pokemonVO.getName());
        pokemon.setSpecies(speciesMarshaller.marshall(pokemonVO.getSpecies()));
        pokemon.setCurrentHp(pokemonVO.getCurrentHp());
        pokemon.setMaxHp(pokemonVO.getMaxHp());
        pokemon.setMoves(pokemonVO.getMoves().stream().map(moveMarshaller::marshall).collect(Collectors.toSet()));
        pokemon.setOriginalTrainer(pokemonVO.getOriginalTrainer());
        return pokemon;
    }

    @Override
    public PokemonVO unmarshall(Pokemon pokemon) {
        Integer id = pokemon.getId();
        String name = pokemon.getName();
        SpeciesVO species = speciesMarshaller.unmarshall(pokemon.getSpecies());
        int currentHp = pokemon.getCurrentHp();
        int maxHp = pokemon.getMaxHp();
        Set<MoveVO> moves = pokemon.getMoves().stream().map(moveMarshaller::unmarshall).collect(Collectors.toSet());
        String originalTrainer = pokemon.getOriginalTrainer();
        return new PokemonVO(id, name, species, currentHp, maxHp, moves, originalTrainer);
    }
}