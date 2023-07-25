package com.pokemon.ao.dto.converter;

import com.pokemon.ao.domain.SpeciesVO;
import com.pokemon.ao.dto.SpeciesDTO;
import com.pokemon.ao.persistence.service.TypeService;
import org.springframework.stereotype.Component;

@Component
public class SpeciesConverterDTO implements ConverterDTO <SpeciesDTO, SpeciesVO> {

    private final TypeService typeService;

    public SpeciesConverterDTO(TypeService typeService) {
        this.typeService = typeService;
    }

    @Override
    public SpeciesVO convertFromDTOToVO(SpeciesDTO speciesDTO) {
        return SpeciesVO.builder()
                .name(speciesDTO.getName())
                .spriteUrl(speciesDTO.getSpriteUrl())
                .type(typeService.findByName(speciesDTO.getType()))
                .build();

    }

    @Override
    public SpeciesDTO convertFromVOToDTO(SpeciesVO speciesVO) {
        return SpeciesDTO.builder()
                .name(speciesVO.getName())
                .spriteUrl(speciesVO.getSpriteUrl())
                .type(speciesVO.getType().getName())
                .build();
    }
}
