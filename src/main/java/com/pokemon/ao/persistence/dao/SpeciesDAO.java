package com.pokemon.ao.persistence.dao;

import com.pokemon.ao.persistence.entity.Species;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpeciesDAO extends JpaRepository<Species, Integer> {

}
