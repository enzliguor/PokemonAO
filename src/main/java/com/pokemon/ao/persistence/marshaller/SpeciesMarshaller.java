package com.pokemon.ao.persistence.marshaller;

import com.pokemon.ao.domain.SpeciesVO;
import com.pokemon.ao.persistence.entity.Species;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SpeciesMarshaller implements Marshaller<SpeciesVO, Species> {

    private final TypeMarshaller typeMarshaller;

    @Autowired
    private SpeciesMarshaller(TypeMarshaller typeMarshaller) {
        this.typeMarshaller = typeMarshaller;
    }

    @Override
    public Species marshall(SpeciesVO speciesVO) {
        return Species.builder()
                .id(speciesVO.getId())
                .name(speciesVO.getName())
                .spriteUrl(speciesVO.getSpriteUrl())
                .type(typeMarshaller.marshall(speciesVO.getType()))
                .build();
    }

    @Override
    public SpeciesVO unmarshall(Species species) {
       return SpeciesVO.builder()
               .id(species.getId())
               .name(species.getName())
               .spriteUrl(species.getSpriteUrl())
               .type(typeMarshaller.unmarshall(species.getType()))
               .build();
    }

}
