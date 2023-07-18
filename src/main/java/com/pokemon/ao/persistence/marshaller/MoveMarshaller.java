package com.pokemon.ao.persistence.marshaller;

import com.pokemon.ao.domain.MoveVO;
import com.pokemon.ao.domain.TypeVO;
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
        Move move = new Move();
        move.setId(moveVO.getId());
        move.setName(moveVO.getName());
        move.setType(typeMarshaller.marshall(moveVO.getType()));
        move.setPower(moveVO.getPower());
        return move;
    }

    @Override
    public MoveVO unmarshall(Move move) {
        Long id = move.getId();
        String name = move.getName();
        TypeVO type = typeMarshaller.unmarshall(move.getType());
        int power = move.getPower();
        return new MoveVO(id, name, type, power);
    }
}
