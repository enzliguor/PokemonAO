package com.pokemon.ao.persistence.service;

import com.pokemon.ao.domain.MoveVO;
import com.pokemon.ao.persistence.entity.Move;
import com.pokemon.ao.persistence.marshaller.Marshaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class MoveService extends AbstractService<MoveVO, Move, Long> {
    @Autowired
    protected MoveService(Marshaller<MoveVO, Move> marshaller, JpaRepository<Move, Long> dao) {
        super(marshaller, dao);
    }
}
