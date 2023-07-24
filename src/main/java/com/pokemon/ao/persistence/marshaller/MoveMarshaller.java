package com.pokemon.ao.persistence.marshaller;

import com.pokemon.ao.domain.MoveVO;
import com.pokemon.ao.persistence.entity.Move;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MoveMarshaller implements Marshaller<MoveVO, Move> {

    private final TypeMarshaller typeMarshaller;

    @Autowired
    private MoveMarshaller(TypeMarshaller typeMarshaller) {
        this.typeMarshaller = typeMarshaller;
    }

    @Override
    public Move marshall(MoveVO moveVO) {
        return Move.builder()
                .id(moveVO.getId())
                .name(moveVO.getName())
                .type(typeMarshaller.marshall(moveVO.getType()))
                .power(moveVO.getPower())
                .build();
    }

    @Override
    public MoveVO unmarshall(Move move) {
        return MoveVO.builder()
                .id(move.getId())
                .name(move.getName())
                .type(typeMarshaller.unmarshall(move.getType()))
                .power(move.getPower())
                .build();
    }
}
