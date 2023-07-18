package com.pokemon.ao.persistence.marshaller;

import com.pokemon.ao.domain.MoveVO;
import com.pokemon.ao.domain.PokemonVO;
import com.pokemon.ao.domain.TypeVO;
import com.pokemon.ao.persistence.entity.Pokemon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PokemonMarshaller implements Marshaller<PokemonVO, Pokemon> {

    private final MoveMarshaller moveMarshaller;
    private final TypeMarshaller typeMarshaller;

    @Autowired
    private PokemonMarshaller(MoveMarshaller moveMarshaller, TypeMarshaller typeMarshaller) {
        this.moveMarshaller = moveMarshaller;
        this.typeMarshaller = typeMarshaller;
    }

    @Override
    public Pokemon marshall(PokemonVO pokemonVO) {
        Pokemon pokemon = new Pokemon();
        pokemon.setId(pokemonVO.getId());
        pokemon.setName(pokemonVO.getName());
        pokemon.setSprite(pokemonVO.getSprite());
        pokemon.setCurrentHp(pokemonVO.getCurrentHp());
        pokemon.setMaxHp(pokemonVO.getMaxHp());
        pokemon.setType(typeMarshaller.marshall(pokemonVO.getType()));
        pokemon.setMoves(pokemonVO.getMoves().stream().map(moveMarshaller::marshall).collect(Collectors.toSet()));
        pokemon.setOriginalTrainer(pokemonVO.getOriginalTrainer());
        return pokemon;
    }

    @Override
    public PokemonVO unmarshall(Pokemon pokemon) {
        Long id = pokemon.getId();
        String name = pokemon.getName();
        String sprite = pokemon.getSprite();
        int currentHp = pokemon.getCurrentHp();
        int maxHp = pokemon.getMaxHp();
        TypeVO type = typeMarshaller.unmarshall(pokemon.getType());
        Set<MoveVO> moves = pokemon.getMoves().stream().map(moveMarshaller::unmarshall).collect(Collectors.toSet());
        String originalTrainer = pokemon.getOriginalTrainer();
        return new PokemonVO(id, name, sprite, currentHp, maxHp, type, moves, originalTrainer);
    }
}