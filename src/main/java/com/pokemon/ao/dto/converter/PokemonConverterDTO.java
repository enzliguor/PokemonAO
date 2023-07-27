package com.pokemon.ao.dto.converter;

import com.pokemon.ao.config.CustomProperties;
import com.pokemon.ao.domain.MoveVO;
import com.pokemon.ao.domain.PokemonVO;
import com.pokemon.ao.domain.SpeciesVO;
import com.pokemon.ao.domain.UnknownPokemonVO;
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

    private final CustomProperties customProperties;

    @Autowired
    public PokemonConverterDTO(SpeciesService speciesService, MoveService moveService, CustomProperties customProperties) {
        this.speciesService = speciesService;
        this.moveService = moveService;
        this.customProperties = customProperties;
    }

    @Override
    public PokemonVO convertFromDTOToVO(PokemonDTO pokemonDTO) {
        PokemonVO pokemonVO = PokemonVO.builder()
                .name(pokemonDTO.getName())
                .species(this.speciesService.findById(pokemonDTO.getSpeciesId()))
                .currentHp(pokemonDTO.getCurrentHp())
                .maxHp(pokemonDTO.getMaxHp())
                .moves(this.moveService.mapMovesIdsToMoveVO(pokemonDTO.getMovesIds()))
                .originalTrainer(pokemonDTO.getOriginalTrainer())
                .build();
        if (pokemonVO.getSpecies() == null) {
            SpeciesVO speciesVO = this.speciesService.findById(this.customProperties.getUnknownSpeciesID());
            pokemonVO.setSpecies(speciesVO);
            UnknownPokemonVO unknownPokemonVO = (UnknownPokemonVO) pokemonVO;
            unknownPokemonVO.setOriginalSpeciesID(pokemonDTO.getSpeciesId());
            return unknownPokemonVO;
        }
        return pokemonVO;
    }

    @Override
    public PokemonDTO convertFromVOToDTO(PokemonVO pokemonVO) {
        return PokemonDTO.builder()
                .name(pokemonVO.getName())
                .speciesId(pokemonVO.getSpecies().getId())
                .currentHp(pokemonVO.getCurrentHp())
                .typeId(pokemonVO.getSpecies().getType().getId())
                .maxHp(pokemonVO.getMaxHp())
                .movesIds(pokemonVO.getMoves()
                        .values().stream()
                        .map(MoveVO::getId)
                        .collect(Collectors.toSet()))
                .originalTrainer(pokemonVO.getOriginalTrainer())
                .build();
    }
}