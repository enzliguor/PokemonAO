package com.pokemon.ao.persistence.service;

import com.pokemon.ao.config.CustomProperties;
import com.pokemon.ao.domain.MoveSlot;
import com.pokemon.ao.domain.MoveVO;
import com.pokemon.ao.persistence.entity.Move;
import com.pokemon.ao.persistence.marshaller.Marshaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class MoveService extends AbstractService<MoveVO, Move, Integer> {

    private final CustomProperties customProperties;
    @Autowired
    protected MoveService(Marshaller<MoveVO, Move> marshaller, JpaRepository<Move, Integer> dao, CustomProperties customProperties) {
        super(marshaller, dao);
        this.customProperties = customProperties;
    }

    public Map<MoveSlot, MoveVO> mapMovesIdsToMoveVO(Set<Integer> movesId ) {
        List<Integer> moves = new ArrayList<>(movesId);
        return IntStream.range(0, MoveSlot.values().length)
                .boxed()
                .collect(Collectors.toMap(
                        index -> MoveSlot.values()[index],
                        index -> {
                            MoveVO moveVO = this.findById(moves.get(index));
                            if(moveVO == null){
                                return this.findById(this.customProperties.getUnknownMoveID());
                            }
                            return moveVO;
                        }
                ));
    }
}
