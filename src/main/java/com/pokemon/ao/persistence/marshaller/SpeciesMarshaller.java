package com.pokemon.ao.persistence.marshaller;

import com.pokemon.ao.domain.SpeciesVO;
import com.pokemon.ao.domain.TypeVO;
import com.pokemon.ao.persistence.entity.Species;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class SpeciesMarshaller implements Marshaller<SpeciesVO, Species>{

    private final TypeMarshaller typeMarshaller;

    @Autowired

    public SpeciesMarshaller(TypeMarshaller typeMarshaller) {
        this.typeMarshaller = typeMarshaller;
    }

    @Override
    public Species marshall(SpeciesVO speciesVO) {
        Species species = new Species();
        species.setId(speciesVO.getId());
        species.setName(speciesVO.getName());
        species.setSpriteUrl(speciesVO.getSpriteUrl());
        species.setType((typeMarshaller.marshall(speciesVO.getType())));
        return species;
    }

    @Override
    public SpeciesVO unmarshall(Species species) {
        Long id = species.getId();
        String name = species.getName();
        String sprite = species.getSpriteUrl();
        TypeVO type = typeMarshaller.unmarshall(species.getType());
        return new SpeciesVO(id, name, sprite, type);
    }

}
