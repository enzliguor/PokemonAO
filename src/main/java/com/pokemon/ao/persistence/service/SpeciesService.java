package com.pokemon.ao.persistence.service;

import com.pokemon.ao.domain.SpeciesVO;
import com.pokemon.ao.persistence.entity.Species;
import com.pokemon.ao.persistence.marshaller.Marshaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class SpeciesService extends AbstractService<SpeciesVO, Species, Long> {
    @Autowired
    protected SpeciesService(Marshaller<SpeciesVO, Species> marshaller, JpaRepository<Species, Long> dao) {
        super(marshaller, dao);
    }

}
