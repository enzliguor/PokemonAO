package com.pokemon.ao.dto.converter;

import com.pokemon.ao.domain.MoveVO;
import com.pokemon.ao.domain.PokemonVO;
import com.pokemon.ao.domain.SpeciesVO;
import com.pokemon.ao.dto.PokemonDTO;
import com.pokemon.ao.persistence.service.MoveService;
import com.pokemon.ao.persistence.service.SpeciesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PokemonConverterDTO implements ConverterDTO<PokemonDTO, PokemonVO> {

    private final SpeciesService speciesService;

    private final MoveService moveService;

    @Autowired
    public PokemonConverterDTO(SpeciesService speciesService , MoveService moveService) {
        this.speciesService = speciesService;
        this.moveService = moveService;
    }

    @Override
    public PokemonVO convertFromDTOToVO(PokemonDTO pokemonDTO){
        SpeciesVO species = this.speciesService.findById(pokemonDTO.getSpeciesId());
        String name = pokemonDTO.getName();
        int currentHp = pokemonDTO.getCurrentHp();
        int maxHp= pokemonDTO.getMaxHp();
        String originalTrainer= pokemonDTO.getOriginalTrainer();
        Set<MoveVO>moves= this.moveService.mapMovesIdsToMoveVO(pokemonDTO.getMovesIds());

        return new PokemonVO(null, name, species, currentHp, maxHp, moves, originalTrainer);
    }

    @Override
    public PokemonDTO convertFromVOToDTO(PokemonVO pokemonVO){
       Integer speciesId = pokemonVO.getSpecies().getId();
       String name = pokemonVO.getName();
       int currentHp = pokemonVO.getCurrentHp();
       int maxHp = pokemonVO.getMaxHp ();
       String originalTrainer = pokemonVO.getOriginalTrainer();
       Set<Integer> moves = pokemonVO.getMoves().stream().map(MoveVO::getId).collect(Collectors.toSet());

       return new PokemonDTO(null, name , speciesId, currentHp, maxHp, moves, originalTrainer);
    }
}