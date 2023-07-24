package com.pokemon.ao.persistence.service;

import com.pokemon.ao.domain.MoveVO;
import com.pokemon.ao.persistence.entity.Move;
import com.pokemon.ao.persistence.marshaller.Marshaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MoveService extends AbstractService<MoveVO, Move, Integer> {
    @Autowired
    protected MoveService(Marshaller<MoveVO, Move> marshaller, JpaRepository<Move, Integer> dao) {
        super(marshaller, dao);
    }

    public Set<MoveVO> mapMovesIdsToMoveVO(Set<Integer> movesId ) {
        return movesId.stream ()
                .map(moveId -> this.findById(moveId))
                .collect(Collectors.toSet());
    }
}
