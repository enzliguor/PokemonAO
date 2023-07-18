package com.pokemon.ao.persistence.service;

import com.pokemon.ao.domain.PokemonVO;
import com.pokemon.ao.persistence.dao.PokemonDAO;
import com.pokemon.ao.persistence.entity.Pokemon;
import com.pokemon.ao.persistence.marshaller.PokemonMarshaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PokemonService {
    private final PokemonDAO pokemonDao;

    private final PokemonMarshaller pokemonMarshaller;

    @Autowired
    public PokemonService(PokemonDAO pokemonDao , PokemonMarshaller pokemonMarshaller) {
        this.pokemonDao = pokemonDao;
        this.pokemonMarshaller = pokemonMarshaller;
    }

    public Pokemon getPokemonById(Long id){
        return pokemonDao.findById(id).orElse(null);
    }

    public Pokemon savePokemonVO(PokemonVO pokemonVO){
        return pokemonDao.save(pokemonMarshaller.marshall(pokemonVO));
    }

    public void deletePokemon(Long id){
        pokemonDao.deleteById(id);
    }
}
