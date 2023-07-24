package com.pokemon.ao.persistence.service;

import com.pokemon.ao.domain.PokemonVO;
import com.pokemon.ao.persistence.dao.PokemonDAO;
import com.pokemon.ao.persistence.entity.Pokemon;
import com.pokemon.ao.persistence.marshaller.Marshaller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PokemonService extends AbstractService<PokemonVO, Pokemon, Long> {
    protected PokemonService(Marshaller<PokemonVO, Pokemon> marshaller, JpaRepository<Pokemon, Long> dao) {
        super(marshaller, dao);
    }

    public List<Long> findAllIds(){
        PokemonDAO pokemonDAO = (PokemonDAO) dao;
        return pokemonDAO.findAllIds();
    }
}
