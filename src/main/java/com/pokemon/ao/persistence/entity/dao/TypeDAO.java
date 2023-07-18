package com.pokemon.ao.persistence.entity.dao;

import com.pokemon.ao.persistence.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeDAO extends JpaRepository<Type, Long> {

}