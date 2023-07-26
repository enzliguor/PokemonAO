package com.pokemon.ao.dto.converter;

import com.pokemon.ao.domain.MoveVO;
import com.pokemon.ao.domain.PokemonVO;
import com.pokemon.ao.dto.PokemonDTO;
import com.pokemon.ao.persistence.service.MoveService;
import com.pokemon.ao.persistence.service.SpeciesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
    public PokemonVO convertFromDTOToVO(PokemonDTO pokemonDTO) {
        return PokemonVO.builder()
                .name(pokemonDTO.getName())
                .species(this.speciesService.findById(pokemonDTO.getSpeciesId()))
                .currentHp(pokemonDTO.getCurrentHp())
                .maxHp(pokemonDTO.getMaxHp())
                .moves(this.moveService.mapMovesIdsToMoveVO(pokemonDTO.getMovesIds()))
                .originalTrainer(pokemonDTO.getOriginalTrainer())
                .build();
    }

    @Override
    public PokemonDTO convertFromVOToDTO(PokemonVO pokemonVO) {
        return PokemonDTO.builder()
                .name(pokemonVO.getName())
                .speciesId(pokemonVO.getSpecies().getId())
                .currentHp(pokemonVO.getCurrentHp())
                .typeId(pokemonVO.getSpecies().getType().getId())
                .maxHp(pokemonVO.getMaxHp())
                .movesIds(pokemonVO.getMoves().stream().map(MoveVO::getId).collect(Collectors.toSet()))
                .originalTrainer(pokemonVO.getOriginalTrainer())
                .build();
    }
}