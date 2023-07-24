package com.pokemon.ao.dto.converter;

import com.pokemon.ao.domain.ValueObject;
import com.pokemon.ao.dto.DTO;

public interface ConverterDTO<D extends DTO , V extends ValueObject>{

    V convertFromDTOToVO(D d);

    D convertFromVOToDTO(V v);
}
