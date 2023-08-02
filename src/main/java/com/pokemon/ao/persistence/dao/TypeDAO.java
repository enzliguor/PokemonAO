package com.pokemon.ao.persistence.dao;

import com.pokemon.ao.persistence.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TypeDAO extends JpaRepository<Type, Integer> {
    public Optional<Type> findByName(String name);
}