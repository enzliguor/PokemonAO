package com.pokemon.ao.persistence.dao;

import com.pokemon.ao.persistence.entity.Pokemon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PokemonDAO extends JpaRepository<Pokemon, Integer> {
    @Query("SELECT p.id FROM Pokemon p")
    List<Integer> findAllIds();
}