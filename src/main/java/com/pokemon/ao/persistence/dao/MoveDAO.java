package com.pokemon.ao.persistence.dao;

import com.pokemon.ao.persistence.entity.Move;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MoveDAO extends JpaRepository<Move, Long> {

    Optional<Move> findById(Long id);

    Move findByName(String name);

}