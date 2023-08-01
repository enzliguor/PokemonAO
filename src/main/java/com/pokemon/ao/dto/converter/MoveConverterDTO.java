package com.pokemon.ao.dto.converter;

import com.pokemon.ao.domain.MoveVO;
import com.pokemon.ao.dto.MoveDTO;
import com.pokemon.ao.persistence.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MoveConverterDTO implements ConverterDTO<MoveDTO, MoveVO> {

    private final TypeService typeService;

    @Autowired
    private MoveConverterDTO(TypeService typeService) {
        this.typeService = typeService;
    }

    @Override
    public MoveVO convertFromDTOToVO(MoveDTO movesDTO) {
        return MoveVO.builder()
                .name(movesDTO.getName())
                .power(movesDTO.getPower())
                .type(this.typeService.findByName(movesDTO.getTypeName()))
                .build();
    }

    @Override
    public MoveDTO convertFromVOToDTO(MoveVO moveVO) {
        return MoveDTO.builder()
                .name(moveVO.getName())
                .power(moveVO.getPower())
                .typeName(moveVO.getType().getName())
                .build();
    }
}
