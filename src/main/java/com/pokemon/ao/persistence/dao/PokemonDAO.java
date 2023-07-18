package com.pokemon.ao.persistence.dao;

import com.pokemon.ao.persistence.entity.Pokemon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PokemonDAO extends JpaRepository<Pokemon, Long> {

    Optional<Pokemon> findById(Long id);

    Pokemon findByName(String name);
}